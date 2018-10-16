package org.bouncycastle.jce.provider.test;

import java.io.StringReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;

import junit.framework.TestCase;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.OpenSSHPrivateKeySpec;
import org.bouncycastle.jce.spec.OpenSSHPublicKeySpec;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.test.SimpleTest;

public class OpenSSHSpecTests
    extends SimpleTest
{
    private static final SecureRandom secureRandom = new SecureRandom();

    public void setUp()
        throws Exception
    {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null)
        {
            Security.addProvider(new BouncyCastleProvider());
        }
    }


    public void testRSA()
        throws Exception
    {


        OpenSSHPublicKeySpec pubSpec = new OpenSSHPublicKeySpec(Base64.decode("AAAAB3NzaC1yc2EAAAADAQABAAAAgQDvh2BophdIp8ojwGZQR0FQ/awowXnV24nAPm+/na8MOUrdySNhOnlek4LAZl82/+Eu2t21XD6hQUiHKAj6XaNFBthTuss7Cz/tA348DLEMHD9wUtT0FXVmsxqN4BfusunbcULxxVWG2z8FvqeaGgc/Unkp9y7/kyf54pPUCBcClw=="));

        OpenSSHPrivateKeySpec privSpec = new OpenSSHPrivateKeySpec(new PemReader(new StringReader("-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXgIBAAKBgQDvh2BophdIp8ojwGZQR0FQ/awowXnV24nAPm+/na8MOUrdySNh\n" +
            "Onlek4LAZl82/+Eu2t21XD6hQUiHKAj6XaNFBthTuss7Cz/tA348DLEMHD9wUtT0\n" +
            "FXVmsxqN4BfusunbcULxxVWG2z8FvqeaGgc/Unkp9y7/kyf54pPUCBcClwIDAQAB\n" +
            "AoGBAOMXYEoXHgAeREE9CkOWKtDUkEJbnF0rNSB0kZIDt5BJSTeYmNh3jdYi2FX9\n" +
            "OMx2MFIx4v0tJZvQvyiUxl5IJJ9ZJsYUWF+6VbcTVwYYfdVzZzP2TNyGmF9/ADZW\n" +
            "wBehqP04uRlYjt94kqb4HoOKF3gJ3LC4uW9xcEltTBeHWCfhAkEA/2biF5St9/Ya\n" +
            "540E4zu/FKPsxLSaT8LWCo9+X7IqIzlBQCB4GjM+nZeTm7eZOkfAFZoxwfiNde/9\n" +
            "qleXXf6B2QJBAPAW+jDBC3QF4/g8n9cDxm/A3ICmcOFSychLSrydk9ZyRPbTRyQC\n" +
            "YlC2mf/pCrO/yO7h189BXyQ3PXOEhnujce8CQQD7gDy0K90EiH0F94AQpA0OLj5B\n" +
            "lfc/BAXycEtpwPBtrzvqAg9C/aNzXIgmly10jqNAoo7NDA2BTcrlq0uLa8xBAkBl\n" +
            "7Hs+I1XnZXDIO4Rn1VRysN9rRj15ipnbDAuoUwUl7tDUMBFteg2e0kZCW/6NHIgC\n" +
            "0aG6fLgVOdY+qi4lYtfFAkEAqqiBgEgSrDmnJLTm6j/Pv1mBA6b9bJbjOqomrDtr\n" +
            "AWTXe+/kSCv/jYYdpNA/tDgAwEmtkWWEie6+SwJB5cXXqg==\n" +
            "-----END RSA PRIVATE KEY-----\n")).readPemObject().getContent());


        byte[] originalMessage = new byte[10];
        secureRandom.nextBytes(originalMessage);

        originalMessage[0] |= 1;

        KeyFactory kpf = KeyFactory.getInstance("RSA", "BC");

        PublicKey pk = kpf.generatePublic(pubSpec);
        PrivateKey prk = kpf.generatePrivate(privSpec);

        Cipher cipher = Cipher.getInstance("RSA", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, pk);

        byte[] ct = cipher.doFinal(originalMessage);
        cipher.init(Cipher.DECRYPT_MODE, prk);
        byte[] result = cipher.doFinal(ct);

        TestCase.assertTrue("Result did not match original message", Arrays.areEqual(originalMessage, result));


    }


    public void testEncodingRSA()
        throws Exception
    {

        byte[] rawPub = Base64.decode("AAAAB3NzaC1yc2EAAAADAQABAAAAgQDvh2BophdIp8ojwGZQR0FQ/awowXnV24nAPm+/na8MOUrdySNhOnlek4LAZl82/+Eu2t21XD6hQUiHKAj6XaNFBthTuss7Cz/tA348DLEMHD9wUtT0FXVmsxqN4BfusunbcULxxVWG2z8FvqeaGgc/Unkp9y7/kyf54pPUCBcClw==");
        byte[] rawPriv = new PemReader(new StringReader("-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXgIBAAKBgQDvh2BophdIp8ojwGZQR0FQ/awowXnV24nAPm+/na8MOUrdySNh\n" +
            "Onlek4LAZl82/+Eu2t21XD6hQUiHKAj6XaNFBthTuss7Cz/tA348DLEMHD9wUtT0\n" +
            "FXVmsxqN4BfusunbcULxxVWG2z8FvqeaGgc/Unkp9y7/kyf54pPUCBcClwIDAQAB\n" +
            "AoGBAOMXYEoXHgAeREE9CkOWKtDUkEJbnF0rNSB0kZIDt5BJSTeYmNh3jdYi2FX9\n" +
            "OMx2MFIx4v0tJZvQvyiUxl5IJJ9ZJsYUWF+6VbcTVwYYfdVzZzP2TNyGmF9/ADZW\n" +
            "wBehqP04uRlYjt94kqb4HoOKF3gJ3LC4uW9xcEltTBeHWCfhAkEA/2biF5St9/Ya\n" +
            "540E4zu/FKPsxLSaT8LWCo9+X7IqIzlBQCB4GjM+nZeTm7eZOkfAFZoxwfiNde/9\n" +
            "qleXXf6B2QJBAPAW+jDBC3QF4/g8n9cDxm/A3ICmcOFSychLSrydk9ZyRPbTRyQC\n" +
            "YlC2mf/pCrO/yO7h189BXyQ3PXOEhnujce8CQQD7gDy0K90EiH0F94AQpA0OLj5B\n" +
            "lfc/BAXycEtpwPBtrzvqAg9C/aNzXIgmly10jqNAoo7NDA2BTcrlq0uLa8xBAkBl\n" +
            "7Hs+I1XnZXDIO4Rn1VRysN9rRj15ipnbDAuoUwUl7tDUMBFteg2e0kZCW/6NHIgC\n" +
            "0aG6fLgVOdY+qi4lYtfFAkEAqqiBgEgSrDmnJLTm6j/Pv1mBA6b9bJbjOqomrDtr\n" +
            "AWTXe+/kSCv/jYYdpNA/tDgAwEmtkWWEie6+SwJB5cXXqg==\n" +
            "-----END RSA PRIVATE KEY-----\n")).readPemObject().getContent();


        OpenSSHPublicKeySpec pubSpec = new OpenSSHPublicKeySpec(rawPub);
        OpenSSHPrivateKeySpec privSpec = new OpenSSHPrivateKeySpec(rawPriv);


        byte[] originalMessage = new byte[10];
        secureRandom.nextBytes(originalMessage);

        originalMessage[0] |= 1;

        KeyFactory kpf = KeyFactory.getInstance("RSA", "BC");

        PublicKey pk = kpf.generatePublic(pubSpec);
        PrivateKey prk = kpf.generatePrivate(privSpec);


        OpenSSHPublicKeySpec rcPublicKeySpec = kpf.getKeySpec(pk, OpenSSHPublicKeySpec.class);
        OpenSSHPrivateKeySpec rcPrivateSpec = kpf.getKeySpec(prk, OpenSSHPrivateKeySpec.class);


        System.out.println(Hex.toHexString(rcPrivateSpec.getEncoded()));
        System.out.println(Hex.toHexString(rawPriv));


        TestCase.assertTrue("Public key not same", Arrays.areEqual(rawPub, rcPublicKeySpec.getEncoded()));
        TestCase.assertTrue("Private key not same", Arrays.areEqual(rawPriv, rcPrivateSpec.getEncoded()));


    }



    public void testEncodingDSA()
        throws Exception
    {

        byte[] rawPub = Base64.decode("AAAAB3NzaC1kc3MAAACBAJBB5+S4kZZYZLswaQ/zm3GM7YWmHsumwo/Xxu+z6Cg2l5PUoiBBZ4ET9EhhQuL2ja/zrCMCi0ZwiSRuSp36ayPrHLbNJb3VdOuJg8xExRa6F3YfVZfcTPUEKh6FU72fI31HrQmi4rpyHnWxL/iDX496ZG2Hdq6UkPISQpQwj4TtAAAAFQCP9TXcVahR/2rpfEhvdXR0PfhbRwAAAIBdXzAVqoOtb9zog6lNF1cGS1S06W9W/clvuwq2xF1s3bkoI/xUbFSc0IAPsGl2kcB61PAZqcop50lgpvYzt8cq/tbqz3ypq1dCQ0xdmJHj975QsRFax+w6xQ0kgpBhwcS2EOizKb+C+tRzndGpcDSoSMuVXp9i4wn5pJSTZxAYFQAAAIEAhQZc687zYxrEDR/1q6m4hw5GFxuVvLsC+bSHtMF0c11Qy4IPg7mBeP7K5Kq4WyJPtmZhuc5Bb12bJQR6qgd1uLn692fe1UK2kM6eWXBzhlzZ54BslfSKHGNN4qH+ln3Zaf/4rpKE7fvoinkrgkOZmj0PMx9D6wlpHKkXMUxeXtc=");
        byte[] rawPriv = new PemReader(new StringReader("-----BEGIN DSA PRIVATE KEY-----\n" +
            "MIIBuwIBAAKBgQCQQefkuJGWWGS7MGkP85txjO2Fph7LpsKP18bvs+goNpeT1KIg\n" +
            "QWeBE/RIYULi9o2v86wjAotGcIkkbkqd+msj6xy2zSW91XTriYPMRMUWuhd2H1WX\n" +
            "3Ez1BCoehVO9nyN9R60JouK6ch51sS/4g1+PemRth3aulJDyEkKUMI+E7QIVAI/1\n" +
            "NdxVqFH/aul8SG91dHQ9+FtHAoGAXV8wFaqDrW/c6IOpTRdXBktUtOlvVv3Jb7sK\n" +
            "tsRdbN25KCP8VGxUnNCAD7BpdpHAetTwGanKKedJYKb2M7fHKv7W6s98qatXQkNM\n" +
            "XZiR4/e+ULERWsfsOsUNJIKQYcHEthDosym/gvrUc53RqXA0qEjLlV6fYuMJ+aSU\n" +
            "k2cQGBUCgYEAhQZc687zYxrEDR/1q6m4hw5GFxuVvLsC+bSHtMF0c11Qy4IPg7mB\n" +
            "eP7K5Kq4WyJPtmZhuc5Bb12bJQR6qgd1uLn692fe1UK2kM6eWXBzhlzZ54BslfSK\n" +
            "HGNN4qH+ln3Zaf/4rpKE7fvoinkrgkOZmj0PMx9D6wlpHKkXMUxeXtcCFELnLOJ8\n" +
            "D0akSCUFY/iDLo/KnOIH\n" +
            "-----END DSA PRIVATE KEY-----\n")).readPemObject().getContent();


        OpenSSHPublicKeySpec pubSpec = new OpenSSHPublicKeySpec(rawPub);
        OpenSSHPrivateKeySpec privSpec = new OpenSSHPrivateKeySpec(rawPriv);


        byte[] originalMessage = new byte[10];
        secureRandom.nextBytes(originalMessage);

        originalMessage[0] |= 1;

        KeyFactory kpf = KeyFactory.getInstance("DSA", "BC");

        PublicKey pk = kpf.generatePublic(pubSpec);
        PrivateKey prk = kpf.generatePrivate(privSpec);


        OpenSSHPublicKeySpec rcPublicKeySpec = kpf.getKeySpec(pk, OpenSSHPublicKeySpec.class);
        OpenSSHPrivateKeySpec rcPrivateSpec = kpf.getKeySpec(prk, OpenSSHPrivateKeySpec.class);


        System.out.println(Hex.toHexString(rcPrivateSpec.getEncoded()));
        System.out.println(Hex.toHexString(rawPriv));


        TestCase.assertTrue("Public key not same", Arrays.areEqual(rawPub, rcPublicKeySpec.getEncoded()));
        TestCase.assertTrue("Private key not same", Arrays.areEqual(rawPriv, rcPrivateSpec.getEncoded()));


    }







    public void testEncodingECDSA()
        throws Exception
    {
        byte[] rawPub = Base64.decode("AAAAE2VjZHNhLXNoYTItbmlzdHAyNTYAAAAIbmlzdHAyNTYAAABBBHq5qxGqnh93Gpbj2w1Avx1UwBl6z5bZC3Viog1yNHDZYcV6Da4YQ3i0/hN7xY7sUy9dNF6g16tJSYXQQ4tvO3g=");
        byte[] rawPriv = new PemReader(new StringReader("-----BEGIN EC PRIVATE KEY-----\n" +
            "MHcCAQEEIHeg/+m02j6nr4bO8ubfbzhs0fqOjiuIoWbvGnVg+FmpoAoGCCqGSM49\n" +
            "AwEHoUQDQgAEermrEaqeH3caluPbDUC/HVTAGXrPltkLdWKiDXI0cNlhxXoNrhhD\n" +
            "eLT+E3vFjuxTL100XqDXq0lJhdBDi287eA==\n" +
            "-----END EC PRIVATE KEY-----\n")).readPemObject().getContent();


        OpenSSHPublicKeySpec pubSpec = new OpenSSHPublicKeySpec(rawPub);
        OpenSSHPrivateKeySpec privSpec = new OpenSSHPrivateKeySpec(rawPriv);


        KeyFactory kpf = KeyFactory.getInstance("EC", "BC");

        PublicKey pk = kpf.generatePublic(pubSpec);
        PrivateKey prk = kpf.generatePrivate(privSpec);


        OpenSSHPublicKeySpec ecdsaPublicKeySpec = kpf.getKeySpec(pk, OpenSSHPublicKeySpec.class);
        OpenSSHPrivateKeySpec ecdsaPrivateSpec = kpf.getKeySpec(prk, OpenSSHPrivateKeySpec.class);

        System.out.println(Hex.toHexString(ecdsaPrivateSpec.getEncoded()));
        System.out.println(Hex.toHexString(rawPriv));



        TestCase.assertTrue("Public key not same", Arrays.areEqual(rawPub, ecdsaPublicKeySpec.getEncoded()));
        TestCase.assertTrue("Private key not same", Arrays.areEqual(rawPriv, ecdsaPrivateSpec.getEncoded()));

    }

    public String getName()
    {
        return "OpenSSHSpec";
    }

    public void performTest()
        throws Exception
    {

    }
}