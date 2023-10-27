///[SinErrores]
// 
class A{
	int x;
	void method(){
		if(4>3){
			var a = 3;
		} else {
			if ( 3 > 2) {
				var a = 3;
			}
			var a = 4;
		}
		while(true) {
			var a = 2;
		}
	}
	int calculate(int a) {
 		return x * a;	
	}
	static void main(){}
}
