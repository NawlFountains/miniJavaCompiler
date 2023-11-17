///339&exitosamente

class A {
	int a;

	int m1() {

		a = 3;
		if (a < 4) {
			debugPrint(a);
		} else {
			debugPrint(-1);
		}
		return a*3;
	}
}
class Init{
    static void main()
    { 
	    var a = new A();
	    a.m1();
	    debugPrint(a.m1());
    }
}


