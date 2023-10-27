///[SinErrores]
// 
class A{
	int calculate() {
		var aux = new C();
		var auxClass = new B();
		aux = auxClass;
	}
	static void main(){}
}
class B extends C {
	
}
class C {
}
