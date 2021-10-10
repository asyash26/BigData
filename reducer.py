#!/usr/bin/env python
"""reducer.py"""


# import sys


def reducer(values):
    result_var = 0
    result_mean = 0
    result_size = 0

    # for line in sys.stdin:
    for line in values:
        size, mean, var = list(map(float, line.split(' ')))
        denominator = result_size + size
        numerator = size * var + result_var * result_size
        term2 = ((mean - result_mean) / denominator) ** 2
        result_var = numerator / denominator + size * result_size * term2
        result_mean = (result_mean * result_size + size * mean) / (result_size + size)
        result_size += size

    return f"Mean: {result_mean}; Variance: {result_var}"


if __name__ == '__main__':
    print(reducer())
