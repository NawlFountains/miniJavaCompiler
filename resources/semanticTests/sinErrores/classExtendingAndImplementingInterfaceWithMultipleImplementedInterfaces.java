///[SinErrores]
//Control simple de declaracion de clases con nombres validos

interface A{
	void methodA(int x, float y);
}

interface B{
	float methodB(float x);
}

interface D extends A, B{
	int methodC();

}

class Init extends C implements D{
	void methodA(int z, float klk){} 
	float methodB(float z){}
	int methodC(){}
    static void main()
    { }
}
class C {
	Object methodFromC(){}
}
