// Acceso simple a una variable de instancia
// Este caso tambien chequea la asignacion multiple

class A {
    int a1;
    int a2;
    
     void m1(){
        a2 = a1 = 10;
    }
    

}


class B extends A{
    
}


class Init{
    static void main()
    { }
}


