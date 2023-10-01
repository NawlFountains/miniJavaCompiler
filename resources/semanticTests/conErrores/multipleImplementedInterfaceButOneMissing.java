///[Error:methodD|11]
// 
class A extends B implements C,D{
	void methodB() {}
	static void main() {}
}
interface C {
	void methodB();
}
interface D {
	Object methodD(System s);
}
class B {}
