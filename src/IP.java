public class IP {

    private Config config;
    private String adresse;

    public IP(String adresse, Config config){
        this.adresse = adresse;
        this.config = config;
    }

    public boolean accept(){
        boolean res = false;
        if(this.config.getReject() == null) {
            res = true;
        }else{
            String[] reject = this.config.getReject().split("/");
            String[] ip = reject[0].split(".");
            int masque = Integer.parseInt(reject[1]);
            int[] adr = new int[ip.length];
            for (int i = 0; i < ip.length; i++) {
                adr[i] = Integer.parseInt(ip[i]);
            }


        }

        return res;
    }



}
