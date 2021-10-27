import regression.LinearRegression
import data.DataHelper
import metrics.Metrics


object Main {
  def main(args: Array[String]): Unit = {
    val dataHelper = new DataHelper
    val (x_train, y_train) = dataHelper.loadSplitData("/src/main/data/train.csv", 6)

    val linReg = new LinearRegression
    linReg.fit(x_train, y_train)

    val (x_test, y_test) = dataHelper.loadSplitData("/src/main/data/test.csv", 6)
    val preds = linReg.predict(x_test)

    val metrics = new Metrics
    println(metrics.mae(preds, y_test))
    println(metrics.mse(preds, y_test))

    dataHelper.writeResult("/src/main/data/predictions.csv", preds)
  }
}