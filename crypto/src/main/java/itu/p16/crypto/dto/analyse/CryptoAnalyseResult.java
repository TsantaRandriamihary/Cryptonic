package itu.p16.crypto.dto.analyse;


public class CryptoAnalyseResult {
    private int idCrypto;
    private String cryptoNom;
    private String cryptoSymbole;
    private double min;
    private double max;
    private double moyenne;
    private double ecartType;
    private double premierQuartile;

    // Constructeur
    public CryptoAnalyseResult(int idCrypto, String cryptoNom, String cryptoSymbole, 
                               double min, double max, double moyenne, double ecartType, 
                               double premierQuartile) {
        this.idCrypto = idCrypto;
        this.cryptoNom = cryptoNom;
        this.cryptoSymbole = cryptoSymbole;
        this.min = min;
        this.max = max;
        this.moyenne = moyenne;
        this.ecartType = ecartType;
        this.premierQuartile = premierQuartile;
    }

    // Getters et Setters
    public int getIdCrypto() { return idCrypto; }
    public String getCryptoNom() { return cryptoNom; }
    public String getCryptoSymbole() { return cryptoSymbole; }
    public double getMin() { return min; }
    public double getMax() { return max; }
    public double getMoyenne() { return moyenne; }
    public double getEcartType() { return ecartType; }
    public double getPremierQuartile() { return premierQuartile; }
}
