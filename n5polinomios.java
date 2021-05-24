import java.util.Scanner;

public class n5polinomios{

    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int p = sc.nextInt();
        Polinomio p1 = new Polinomio(p);
        Polinomio p2 = new Polinomio(p);
        int n1 = sc.nextInt();
        int n2 = sc.nextInt();
        int c;
        int expx;
        int expy;
       
        for(int i=0;i<n1;i++){
            c= sc.nextInt();
            expx= sc.nextInt();
            expy= sc.nextInt();
            p1.inserta(new Zp(c,p), expx, expy);
        }
        for(int i=0;i<n2;i++){
            c= sc.nextInt();
            expx= sc.nextInt();
            expy= sc.nextInt();
            p2.inserta(new Zp(c,p), expx, expy);
        }
        System.out.print("Primer polinomio \n f=");
        p1.escribepolinomio();
        System.out.print("Segundo polinomio\n g=");
        p2.escribepolinomio();
        System.out.print("Suma\n f+g=");
        p1.suma(p2).escribepolinomio();
        System.out.print("Resta\n f-g=");
        p1.resta(p2).escribepolinomio();
        System.out.print("Producto\n f*g=");
        p1.producto(p2).escribepolinomio();
        if(p1.cociente(p2)!=null){
            System.out.print("Cociente\n q=");
           Polinomio q=p1.cociente(p2)[0];
           q.escribepolinomio();
            System.out.print("Resuido\n r=");
            Polinomio r=p1.cociente(p2)[1];
            r.escribepolinomio();
        }else
            System.out.println("No se puede resolver el cociente");
            
    }
}
class Polinomio {

    private Monomio principal;
    private int p;

    public Polinomio(int p) {
        principal = null;
        this.p = p;
    }

    public Polinomio(Monomio P) {
        principal = P;
        this.p = P.obtencoeficiente().obtenP();
    }
   
    public Polinomio copia(){
        Polinomio ret = new Polinomio(this.p);
        
        Monomio aux = principal;
        if(aux==null)
            return ret;
        while(aux!=null){
            Zp coef  = aux.obtencoeficiente();
            int expx = aux.obtenexponentex();
            int expy = aux.obtenexponentey();
            ret.inserta(coef, expx, expy);
            aux=aux.obtensig();
        }
        return ret;
    }
    
    private Monomio obtenprincipal() {
        return principal;
    }

    public void inserta(Zp coeficiente, int exponentex, int exponentey) {
        if (coeficiente.escero()) return;
        Monomio aux = new Monomio(coeficiente, exponentex, exponentey);
        if (principal == null) {
            principal = aux;
            return;
        }
        if (principal.grado() < aux.grado()) {
            aux.apuntasig(principal);
            principal = aux;
            return;
        }
        if (principal.grado() == aux.grado()) {
            if (principal.obtenexponentex() == exponentex) {
                if (principal.obtencoeficiente().suma(coeficiente).escero()) {
                    principal = principal.obtensig();
                    return;
                } else {
                    principal.sumacoeficiente(coeficiente);
                    return;
                }
            }
            if (principal.obtenexponentex() < exponentex) {
                aux.apuntasig(principal);
                principal = aux;
                return;
            } else {
                Monomio paux = principal;
                while (paux.obtensig() != null) {
                    if (aux.obtenexponentex() > paux.obtensig().obtenexponentex()) {
                        aux.apuntasig(paux.obtensig());
                        paux.apuntasig(aux);
                        return;
                    } else {
                        paux = paux.obtensig();
                    }
                }

            }
        }

        Monomio paux = principal;
        while (paux.obtensig() != null) {
            if (aux.grado() > paux.obtensig().grado()) {
                aux.apuntasig(paux.obtensig());
                paux.apuntasig(aux);
                return;
            }
            if (aux.grado() == paux.obtensig().grado()) {
                if (aux.obtenexponentex() == paux.obtensig().obtenexponentex()) {
                    if (paux.obtensig().obtencoeficiente().suma(coeficiente).escero()) {
                        paux.apuntasig(paux.obtensig().obtensig());
                        return;
                    } else {
                        paux.obtensig().sumacoeficiente(coeficiente);
                        return;
                    }
                } else {
                    Monomio paux2 = paux.obtensig();
                    while (paux2.obtensig() != null) {
                        if (aux.obtenexponentex() > paux2.obtensig().obtenexponentex()) {
                            aux.apuntasig(paux2.obtensig());
                            paux2.apuntasig(aux);
                            return;
                        } else {
                            paux2 = paux2.obtensig();
                        }
                    }
                }
            }
            paux = paux.obtensig();
        }
        paux.apuntasig(aux);
    }

    public void escribepolinomio() {
        if (principal == null) {
            System.out.println("0(x,y)");
            return;
        }
        Monomio paux = principal;
        while (paux != null) {
            System.out.print("+");
            int expx = paux.obtenexponentex();
            int expy = paux.obtenexponentey();
            paux.obtencoeficiente().escribeZp();

            if (expx == 1) {
                System.out.print("x");
            }
            if (expx > 1) {
                System.out.format("x^%d", paux.obtenexponentex());
            }
            if (expy == 1) {
                System.out.print("y");
            }
            if (expy > 1) {
                System.out.format("y^%d", paux.obtenexponentey());
            }
            
            paux = paux.obtensig();
        }
        System.out.println();
    }

    public Polinomio suma(Polinomio sumando) {
        Polinomio pol = new Polinomio(this.p);
        Monomio paux = this.principal;
        while (paux != null) {
            pol.inserta(paux.obtencoeficiente(), paux.obtenexponentex(), paux.obtenexponentey());
            paux = paux.obtensig();
        }
        paux = sumando.obtenprincipal();
        while (paux != null) {
            pol.inserta(paux.obtencoeficiente(), paux.obtenexponentex(), paux.obtenexponentey());
            paux = paux.obtensig();
        }
        return pol;
    }

    public Polinomio resta(Polinomio sumando) {
        Polinomio pol = new Polinomio(this.p);
        Monomio paux = principal;
        while (paux != null) {
            pol.inserta(paux.obtencoeficiente(), paux.obtenexponentex(), paux.obtenexponentey());
            paux = paux.obtensig();
        }
        paux = sumando.obtenprincipal();
        while (paux != null) {
            pol.inserta(paux.obtencoeficiente().inverso(), paux.obtenexponentex(), paux.obtenexponentey());
            paux = paux.obtensig();
        }
        return pol;
    }

    public Polinomio producto(Polinomio q){
            Polinomio pol=new Polinomio(this.p);
            if(principal==null )return pol;
            if(q.principal==null)return pol;
            Monomio aux1=principal;
            while(aux1!=null){
                Monomio aux2=q.obtenprincipal();
                while(aux2!=null){
                        Zp coef  = aux2.obtencoeficiente().producto(aux1.obtencoeficiente());
                        int expx = aux2.obtenexponentex()+aux1.obtenexponentex();
                        int expy = aux2.obtenexponentey()+aux1.obtenexponentey();
                        pol.inserta(coef,expx,expy);
                        aux2=aux2.obtensig();
                        }
                aux1=aux1.obtensig();
                }
            return pol;
            }

    public Polinomio[] cociente(Polinomio divisor)
        {
        if(this.obtenprincipal().grado()<divisor.obtenprincipal().grado())
            return null;
        if(this.variable()=='0' || divisor.variable()=='0' || this.variable()!=divisor.variable())
            return null;
        Polinomio q = new Polinomio(this.p);
        Polinomio r = this.copia();   
        while(r.obtenprincipal()!=null && r.obtenprincipal().grado() >= divisor.obtenprincipal().grado()){
            int exp = r.obtenprincipal().grado() - divisor.obtenprincipal().grado();
            Zp coef = r.obtenprincipal().obtencoeficiente().divide(divisor.obtenprincipal().obtencoeficiente());
            q.inserta(coef, exp, 0);
            Polinomio aux = new Polinomio(new Monomio(coef, exp, 0));
            Polinomio aux2 = divisor.producto(aux);
            r = r.resta(aux2);
        }
        Polinomio[] ret = new Polinomio[2];
        ret[0]=q;
        ret[1]=r;
        return ret;
        }
    public char variable (){
        Monomio aux = principal;
        char ret='0'; //Si es cero tiene dos variables o ni una  
        if(aux==null){
            return '0';
        }
        
        while(aux!=null){
            if(aux.obtenexponentex()>0)
                ret='x';
            if(aux.obtenexponentey()>0 && ret=='x')
                    return '0';
            aux=aux.obtensig();
        }
        while(aux!=null){
            if(aux.obtenexponentey()>0)
                ret='y';
            if(aux.obtenexponentex()>0 && ret=='y')
                    return '0';
            aux=aux.obtensig();
        }
        return ret;
       
    }
}


class Monomio {
            
    private int exponentex;
    private int exponentey;
    private Zp coeficiente;
    private Monomio sig;
    public Monomio(Zp coeficiente, int exponentex,int exponentey)
        {
        this.exponentex =exponentex;
        this.exponentey =exponentey;
        this.coeficiente=coeficiente;
        this.sig=null;
        }
    public int obtenexponentex(){
                return exponentex; 
                }
    public int obtenexponentey(){
                return exponentey; 
                }
    public Zp obtencoeficiente(){
                return coeficiente; 
                }
    public void sumacoeficiente(Zp  sumando){
                 coeficiente=coeficiente.suma(sumando);
                }
    public Monomio obtensig(){
                return sig;
                }
    public void apuntasig(Monomio sig){
                this.sig=sig;
                }
    public int grado(){
        return exponentex+exponentey;
    }
   
}


class Zp {
    protected int z;
    protected int p;
    
    public Zp(int z,int p){
        if(z>=0){
            this.p=p;
            this.z=z%p;
        }
        else{
            int aux = -z;
            aux=aux%p;
            this.z=(p-aux)%p;
            this.p=p;
        }
    }
        
    public int obtenZ(){
        return z;
    }
    public int obtenP(){
        return p;
    }
    
    public Zp suma(Zp sumando){
        int suma=obtenZ()+sumando.obtenZ();
        return new Zp(suma,p);
    }
    
    public Zp producto(Zp producto){
        int ret = obtenZ()*producto.obtenZ();
        return new Zp(ret,p);
    }

    public Zp divide(Zp dividendo){
        int i;
        for(i=0;i<p;i++)
            if( (dividendo.obtenZ() * ((i%p) % p))%p == 1)
                break;
        Zp aux = new Zp(i,p);
        
        return this.producto(aux);
    }
    
    public boolean escero(){
                return z==0;
            }
    public void escribeZp(){
        System.out.print(z);
    }
    public Zp inverso(){
        int aux = (p-z)%p;
        Zp ret=new Zp(aux,p);
        return ret;
    }
}
