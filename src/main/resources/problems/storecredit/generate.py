#!/usr/bin/python

import sys
import random


def generateTest(total, nb):
	h = {}
	l = []
	rnd = random.Random()

	while(len(l) < nb - 2):
		n = rnd.randint(1, total*2)

		if (total -n) in h:
			#	Skip, don't want a solution.
			#
			continue
		
		h[n] = 1
		l.append(str(n))
	
	#	Now add a solution:
	#
	n = rnd.randint(1, total - 1)
	l.append(str(n))
	l.append(str(total - n))
	rnd.shuffle(l)

	print total
	print len(l)
	print " ".join(l)
		



if __name__ == "__main__":
	if len(sys.argv) != 4:
		print "Usage: generate.py nbTests total nbItems"
		sys.exit(1)
	(nbTests, total, nbItems) = [ int(x) for x in sys.argv[1:]]

	print nbTests
	for i in range(nbTests):
		generateTest(total, nbItems)






