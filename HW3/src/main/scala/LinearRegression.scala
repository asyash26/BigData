package regression

import breeze.linalg.{DenseMatrix, DenseVector, pinv}

class LinearRegression {

  private var weights: Option[breeze.linalg.DenseVector[Double]] = None

  def fit(x: DenseMatrix[Double], y: DenseVector[Double]): Unit = {
    this.weights = Option(pinv(x) * y.toDenseVector)
  }

  def predict(x: DenseMatrix[Double]): DenseVector[Double] = {
    if (this.weights == None) {
      print("You need to fit the model first")
      DenseVector(0)
    }
    else {
      x * this.weights.get
    }
  }

}
