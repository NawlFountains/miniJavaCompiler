///[Error:methodB|9]
//Falta implementar el metodo methodB

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
	int methodC(){}
    static void main()
    { }
}
class C {
	Object methodFromC(){}
}
