///IterationIterationIteration&exitosamente

class A {
	int a;

	void m1() {
		a = 0;
		while (a < 3) {
			System.printS("Iteration");
			a = a + 1;
		}
	}
}
class Init{
    static void main()
    { 
	    var a = new A();
	    a.m1();
    }
}


