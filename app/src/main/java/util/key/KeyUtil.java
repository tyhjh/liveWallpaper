package util.key;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

/**
 * Created by Tyhj on 2017/5/29.
 */
public class KeyUtil {

    public static final String KEY_ALGORTHM="RSA";//
    public static final String SIGNATURE_ALGORITHM="MD5withRSA";

    public static final String PUBLIC_KEY = "RSAPublicKey";//公钥
    public static final String PRIVATE_KEY = "RSAPrivateKey";//私钥


    public static final String KEY_SHA="SHA";
    public static final String KEY_MD5="MD5";

    public static final String KEY="";

    public static final String SIGN="";

    /**
     * BASE64解密
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception{
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * BASE64加密
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(byte[] key)throws Exception{
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    /**
     * MD5加密
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data)throws Exception{
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();
    }

    /**
     * SHA加密
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data)throws Exception{
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }


    /**
     * 初始化密钥
     * @return
     * @throws Exception
     */
    public static Map<String,Object> initKey()throws Exception{
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORTHM);
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey =  (RSAPrivateKey) keyPair.getPrivate();

        Map<String,Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        return keyMap;
    }


    /**
     * 取得公钥，并转化为String类型
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPublicKey(Map<String, Object> keyMap)throws Exception{
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 取得私钥，并转化为String类型
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getPrivateKey(Map<String, Object> keyMap) throws Exception{
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }


    /**
     * 用私钥加密
     * @param data	加密数据
     * @param key	密钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data,String key)throws Exception{
        //解密密钥
        byte[] keyBytes = decryptBASE64(key);
        //取私钥
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        //对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }


    /**
     * 用私钥解密 * @param data 	加密数据
     * @param key	密钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data,String key)throws Exception{
        //对私钥解密
        byte[] keyBytes = decryptBASE64(key);

        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }


    /**
     * 用公钥加密
     * @param data	加密数据
     * @param key	密钥
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data,String key)throws Exception{
        //对公钥解密
        byte[] keyBytes = decryptBASE64(key);
        //取公钥
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }


    /**
     * 用公钥解密
     * @param data	加密数据
     * @param key	密钥
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data,String key)throws Exception{
        //对私钥解密
        byte[] keyBytes = decryptBASE64(key);
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        Key publicKey = keyFactory.generatePublic(x509EncodedKeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);
    }

    /**
     *	用私钥对信息生成数字签名
     * @param data	//加密数据
     * @param privateKey	//私钥
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data,String privateKey)throws Exception{
        //解密私钥
        byte[] keyBytes = decryptBASE64(privateKey);
        //构造PKCS8EncodedKeySpec对象
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取私钥匙对象
        PrivateKey privateKey2 = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        //用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey2);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     * @param data	加密数据
     * @param publicKey	公钥
     * @param sign	数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data,String publicKey,String sign)throws Exception{
        //解密公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        //构造X509EncodedKeySpec对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM);
        //取公钥匙对象
        PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey2);
        signature.update(data);
        //验证签名是否正常
        return signature.verify(decryptBASE64(sign));

    }


    public static void main(String args[]){
        try {
            Map<String, Object> keyMap=initKey();
            String publicKey=getPublicKey(keyMap);
            String privateKey=getPrivateKey(keyMap);
            System.out.println(publicKey);
            System.out.println(privateKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
