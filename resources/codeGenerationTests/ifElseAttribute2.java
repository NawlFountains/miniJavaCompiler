///-1!false-19&exitosamente

class A {
	int a;

	int m1() {

		a = 3;
		if (a > 4) {
			debugPrint(a);
		} else {
			debugPrint(-1);
		}
		return a*3;
	}
	void m2() {
		if (!false)
			System.printS("!false");
	}
}
class Init{
    static void main()
    { 
	    var a = new A();
	    a.m1();
	    a.m2();
	    debugPrint(a.m1());
    }
}


