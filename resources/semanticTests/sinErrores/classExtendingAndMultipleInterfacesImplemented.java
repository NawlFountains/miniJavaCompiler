///[SinErrores]
//Control simple de declaracion de clases con nombres validos

interface A{
	void methodA(int x, float y);
}

interface B{
	float methodB(float x);
}

class Init extends C implements A,B{
	void methodA(int z, float klk){} 
	float methodB(float z){}
    static void main()
    { }
}
class C {
	Object methodFromC(){}
}
