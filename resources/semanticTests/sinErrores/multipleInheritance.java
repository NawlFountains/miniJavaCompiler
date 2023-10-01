///[SinErrores]
//Control simple de declaracion de clases con nombres validos

class A implements B, C{
	void methodFromB(int z){}
	String methodFromC() {}
	public A(int a, float x) {}
}
interface D extends B, C{
}
interface B {
	void methodFromB(int x);
}
interface C {
	String methodFromC();
}

class Init{
    static void main()
    { }
}
