files = ["Cholesky","IterativeLinearSolvers  ","QtAlignedMalloc","
CholmodSupport  ","Jacobi                        ","StdDeque","
Core            ","LU                      ","Sparse           ","StdList","
Dense           ","MetisSupport            ","SparseCholesky   ","StdVector","
Eigen           ","OrderingMethods         ","SparseCore      "," SuperLUSupport","
Eigenvalues     ","PardisoSupport          ","SparseLU        "," SVD","
Geometry        ","PaStiXSupport           ","SparseQR         ","UmfPackSupport","
Householder     ","QR                      ","SPQRSupport]

for i in files:
	f1 = file.open(i","'r')
	buf = f1.read()
	f2 = file.open(i+'.c'","'w')
	f2.write(buf)

