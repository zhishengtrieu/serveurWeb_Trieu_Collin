import java.net.InetAddress;

public class IP {

    private Config config;
    private InetAddress adresse;

    public IP(InetAddress adresse, Config config) {
        this.adresse = adresse;
        this.config = config;
    }

    public boolean accept() {
        boolean res = true;
        if (this.config.getReject() != null) {
            //on transforme l'addresse client en tableau d'octets
            byte[] adr = this.adresse.getAddress();

            //si on a un ipv4
            if (adr.length == 4) {
                //on recupere l'adresse qu'on rejete et le masque reseau
                String[] reject = this.config.getReject().split("/");
                String[] ip = reject[0].split("\\.");
                int tMasque = Integer.parseInt(reject[1]);

                //on transforme l'adresse et le masque ip en tableau de bits
                int[] bits = new int[32];
                for (int i = 0; i < ip.length; i++) {
                    int octet = Integer.parseInt(ip[i]);
                    String str = Integer.toBinaryString(octet);
                    for (int j = 0; j < str.length(); j++) {
                        bits[i * 8 + (7 - j)] = Integer.parseInt(str.charAt(str.length() - 1 - j) + "");
                    }
                }

                int[] masque = this.masque(tMasque);

                //on compare la premiere et la derniere adresse possible dans le reseau
                //a partir des adresses de reseau et de broadcast en bits
                int[] HostMin = this.network(bits, masque);
                HostMin[HostMin.length - 1] = 1;
                int[] HostMax = this.broadcast(bits, this.wildCard(masque));
                HostMax[HostMax.length - 1] = 0;

                //puis on les transforme en tableaux d'octets
                int[] HostMinByte = this.toByte(HostMin);
                int[] HostMaxByte = this.toByte(HostMax);
                //on teste si l'adresse client est dans l'intervalle de l'adresse de reseau et de broadcast
                int i = 0;
                boolean[] dansIntervalle = new boolean[4];
                while (i < 4) {
                    dansIntervalle[i] = (adr[i] >= HostMinByte[i] && adr[i] <= HostMaxByte[i]);
                    i++;
                }
                //si tous les octets sont dans l'intervale, on rejette l'adresse
                res = !(dansIntervalle[0] && dansIntervalle[1] && dansIntervalle[2] && dansIntervalle[3]);
            }
        }

        return res;
    }

    /**
     * Renvoie le masque sous forme de tableau d'entier representant les bits
     * @param n
     * @return
     */
    public int[] masque(int n) {
        int[] result = new int[32];
        for (int i = 0; i < n; i++) {
            result[i] = 1;
        }
        for (int i = n + 1; i < 32; i++) {
            result[i] = 0;
        }
        return result;
    }

    public int[] wildCard(int[] masque) {
        int[] result = new int[32];
        for (int i = 0; i < masque.length; i++) {
            if (masque[i] == 1) {
                result[i] = 0;
            } else {
                result[i] = 1;
            }
        }
        return result;
    }

    /**
     *Trouve l'adresse de reseau d'une adresse ip avec un masque
     * @param adr
     * @param masque
     * @return
     */
    public int[] network(int[] adr, int[] masque) {
        int[] result = new int[32];
        for (int i = 0; i < adr.length; i++) {
            if (masque[i] == 1) {
                result[i] = adr[i];
            } else {
                result[i] = 0;
            }
        }
        return result;
    }

    /**
     * Trouve l'adresse de broadcast d'un reseau a partir des tableaux de bits d'une l'adresse ip et de la wildcard
     * @param adr
     * @param wildCard
     * @return
     */
    public int[] broadcast(int[] adr, int[] wildCard) {
        int[] result = new int[32];
        for (int i = 0; i < adr.length; i++) {
            if (wildCard[i] == 0) {
                result[i] = adr[i];
            } else {
                result[i] = 1;
            }
        }
        return result;
    }

    /**
     * Convertit un tableau de bits en tableau d'octets sous forme d'entiers
     * @param adr
     * @return
     */
    public int[] toByte(int[] adr) {
        int[] res = new int[4];
        for (int i = 0; i < res.length; i++) {
            res[i] = 0;
            for (int j = 0; j < 8; j++) {
                res[i] += adr[i * 8 + j] * Math.pow(2, 7 - j);
            }
        }
        return res;
    }


}
