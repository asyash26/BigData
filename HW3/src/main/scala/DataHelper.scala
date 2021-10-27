package data
import breeze.io.CSVReader
import breeze.linalg.{DenseMatrix, DenseVector, csvwrite}

import java.io.{File, FileReader}

class DataHelper {

  def loadData(filePath: String): breeze.linalg.DenseMatrix[Double] = {
    val loadedData = CSVReader.read(new FileReader(new File(filePath)), ',', '"', '\\', 1)
    DenseMatrix.tabulate(loadedData.length, loadedData.head.length)((i, j) => loadedData(i)(j).toDouble)
  }

  def splitData(data: breeze.linalg.DenseMatrix[Double], targetIndex: Int): (breeze.linalg.DenseMatrix[Double],  breeze.linalg.DenseVector[Double]) = {
    var nonTargetIndexes = (1 to data(1, ::).inner.length - 1).toIndexedSeq
    nonTargetIndexes = nonTargetIndexes.filter(_ != targetIndex)
    val x = data(::, nonTargetIndexes).toDenseMatrix
    val y = data(::, IndexedSeq(targetIndex)).toDenseMatrix.toDenseVector
    (x, y)
  }

  def loadSplitData(filePath: String, targetIndex: Int): (breeze.linalg.DenseMatrix[Double],  breeze.linalg.DenseVector[Double]) = {
    val loadedData = loadData(filePath)
    val (x, y) = splitData(loadedData, targetIndex)
    (x, y)
  }

  def writeResult(outputFilePath: String, predictions: DenseVector[Double]) = {
    csvwrite(new File(outputFilePath), predictions.toDenseMatrix)
  }

}
