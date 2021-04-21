def mystery2(n):
	if (n <= 1):
		print(": ")
	else:
		print(str((n % 2)) + " ")
		mystery2(n / 2)
		print(str(n) + " ")

mystery2(25)
