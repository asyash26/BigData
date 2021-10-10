#!/usr/bin/env python
"""mapper.py"""


# import sys


def mapper(chunk):
    chunk_size = 0
    chunk_sum = 0
    chunk_square_sum = 0

    # for line in sys.stdin:
    #     value = float(line.split(',')[1])
    for value in chunk[1]:
        chunk_size += 1
        chunk_sum += value
        chunk_square_sum += value * value

    chunk_mean = chunk_sum / chunk_size
    chunk_var = chunk_square_sum / chunk_size - chunk_mean * chunk_mean
    return ' '.join(list(map(str, [chunk_size, chunk_mean, chunk_var])))


if __name__ == '__main__':
    print(mapper())
