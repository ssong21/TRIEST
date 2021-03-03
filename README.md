# TRIEST
An implementation and evaluation of the TRIEST-BASE and TRIEST-IMPR algorithms, as presented in TRIEST: Counting Local and Global Triangles in Fully-Dynamic Streams with Fixed Memory Size. 

Abstract: We present TRIÈST, a suite of one-pass streaming algorithms to compute unbiased, low-variance, high-quality approximations of the global and local (i.e., incident to each vertex) number of triangles in a fully-dynamic graph represented as an adversarial stream of edge insertions and deletions. Our algorithms use reservoir sampling and its variants to exploit the user-specified memory space at all times. This is in contrast with previous approaches, which require hard-to-choose parameters (e.g., a fixed sampling probability) and other no guarantees on the amount of memory they use. We analyze the variance of the estimations and show novel concentration bounds for these quantities. Our experimental results on very large graphs demonstrate that TRIÈST outperforms state-of-the-art approaches in accuracy and exhibits a small update time.
