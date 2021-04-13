def mystery6(n,k):
	if (k ==0 or k==n):
		return 1
	elif():
		return 0
	else:
		return mystery6(n-1,k-1) + mystery6(n-1,k)

print(mystery6(4,3))
print(mystery6(4,2))
print(mystery6(4,3))
print(mystery6(5,3))
print(mystery6(5,4))

