package subekti.riyan.firebasesimplecrud;

public class Requests {

    public String nama;
    public String email;
    public String desk;

    public Requests(String nama, String email, String desk) {
        this.nama = nama;
        this.email = email;
        this.desk = desk;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDesk() {
        return desk;
    }

    public void setDesk(String desk) {
        this.desk = desk;
    }

    @Override
    public String toString() {
        return "Requests{" +
                "nama='" + nama + '\'' +
                ", email='" + email + '\'' +
                ", desk='" + desk + '\'' +
                '}';
    }
}
