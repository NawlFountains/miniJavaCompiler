///33&exitosamente

class A{
    int x;
    
   
      void mc(){
        x = 33;
      }
}


class Init{
    static void main()
    { 
        var a = new A();
	a.mc();
        debugPrint(a.x);
        
    }
}


