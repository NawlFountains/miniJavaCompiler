///[SinErrores]
//Control simple de declaracion de clases con nombres validos

interface A{
	void methodA(int x, float y);
}

interface B extends A{
	float methodB(float x);
}

class Init implements B{
	void methodA(int z, float klk){} 
	float methodB(float z){}
    static void main()
    { }
}
