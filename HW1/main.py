import pandas as pd

from mapper import mapper
from reducer import reducer


def standard(path):
    data = pd.read_csv(path, header=None)
    size = data.shape[0]
    result_mean = data.mean()[1]
    result_var = data.var()[1] * (size - 1) / size
    return f"Mean: {result_mean}; Variance: {result_var}"


def map_reduce(path, chunksize):
    chunks = pd.read_csv(path, chunksize=chunksize, header=None)
    chunks_results = []
    for chunk in chunks:
        chunks_results.append(mapper(chunk))
    map_reduce_results = reducer(chunks_results)
    return map_reduce_results


def main():
    file = open("result.txt", "w")
    file.write(f"Map reduce result: {map_reduce('price.csv', 1000)} \n")
    file.write(f"Standard way result: {standard('price.csv')}")
    file.close()


if __name__ == "__main__":
    main()
