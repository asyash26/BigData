package metrics
import breeze.linalg.{DenseVector, sum}
import breeze.numerics.{abs, pow}

class Metrics {

  def mae(predictions: DenseVector[Double], realValues: DenseVector[Double]): Unit = {
    sum(abs(predictions - realValues)) / realValues.size
  }

  def mse(predictions: DenseVector[Double], realValues: DenseVector[Double]): Unit = {
    sum(pow(predictions - realValues, 2)) / realValues.size
  }

}
