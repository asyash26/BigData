package tf_idf

import org.apache.spark.sql._
import org.apache.spark.sql.functions._


object SparkTfIdf {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder()
      .master("local[*]")
      .appName("hw_4")
      .getOrCreate()

    var data = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("tripadvisor_hotel_reviews.csv")
      .drop("Rating")

    data = data.withColumn("ID", monotonicallyIncreasingId)

    // Привести все к одному регистру
    data = data.withColumn("Review", lower(col("Review")))

    // Удалить все спецсимволы
    data = data.withColumn("Review", regexp_replace(data.col("Review"), "[^a-z ]", ""))

    // Посчитать частоту слова в предложении
    var splitDf = data.withColumn("Review", split(col("Review"), " "))
    val columns = splitDf.columns.map(col) :+
      (explode(col("Review")) as "Token")
    splitDf = splitDf.select(columns: _*)
    val frequency = splitDf.groupBy("ID", "Token")
      .agg(count("Review") as "Tf")
    var tf = splitDf.join(frequency,
      splitDf("ID") === frequency("ID") && splitDf("Token") === frequency("Token"))
      .dropDuplicates()
      .drop(splitDf("ID"))
      .drop(splitDf("Token"))
    tf = tf.withColumn("Tf", col("Tf") / size(col("Review")))
    tf.show(5)

    // Посчитать количество документов со словом
    // Взять только 100 самых встречаемых
    var idf = frequency.groupBy("Token")
      .agg(countDistinct("ID") as "Df")
      .filter(!(column("Token") === ""))
      .orderBy(desc("Df"))
      .limit(100)
    idf = idf.withColumn("Idf", log(lit(data.count()) / col("Df")))
    idf = idf.drop("Df")
    idf.show(5)

    // Сджойнить две полученные таблички и посчитать Tf-Idf (только для слов из предыдущего пункта)
    var tfIdf = tf.join(idf,
      tf("Token") === idf("Token"),
      "inner")
      .drop(idf("Token"))
    tfIdf = tfIdf.withColumn("TfIdf", col("Tf") * col("Idf"))
    tfIdf.show(5)

    // Запайвотить табличку
    // Не придумала как можно более удачно построить сводную таблицу
    // Ограничилась 50 записями для экономии времени
    val pivoted = tfIdf
      .limit(50)
      .groupBy("Token")
      .pivot("ID")
      .avg("TfIdf")
    pivoted.show()
  }
}
