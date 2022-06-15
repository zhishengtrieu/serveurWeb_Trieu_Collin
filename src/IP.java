public class IP {

    private Config config;
    private InetAddress adresse;

    public IP(InetAddress adresse, Config config) {
        this.adresse = adresse;
        this.config = config;
    }

    public boolean accept() {
        boolean res = true;
        if (this.config.getReject() == null) {
            res = true;
        } else {
            byte[] adr = this.adresse.getAddress();

            if (adr.length == 4) {
                //on recupere l'adresse qu'on rejete et le masque reseau
                String[] reject = this.config.getReject().split("/");
                String[] ip = reject[0].split(".");
                int tMasque = Integer.parseInt(reject[1]);

                //on transforme l'adresse et le masque ip en tableau de bits
                int[] bits = new int[32];
                for (int i = 0; i < ip.length; i++) {
                    int octet = Integer.parseInt(ip[i]);
                    String str = Integer.toBinaryString(octet);
                    for (int j = 0; j < str.length(); j++) {
                        bits[i + j + 1] = Integer.parseInt(str.substring(j, j + 1));
                    }
                }
                int[] bitsMasque = this.masque(tMasque);


            }
        }

        return res;
    }

    public int[] masque(int n) {
        int[] result = new int[32];
        for (int i = 0; i < n; i++) {
            result[i] = 1;
        }
        for (int i = n+1; i < 32; i++) {
            result[i] = 0;
        }
        return result;
    }


}
