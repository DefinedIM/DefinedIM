extern crate core;

use jni::objects::JClass;
use jni::objects::JString;
use jni::JNIEnv;

// Blake3 Hash

#[no_mangle]
pub unsafe extern "C" fn Java_org_definedim_crypto_RustSM2Crypto_getBlake3Sum<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass<'a>,
    j_str: JString<'a>,
) -> JString<'a> {
    let str = blake3::hash(env.get_string(&j_str).unwrap().to_bytes()).to_string();
    return env.new_string(str).unwrap();
}

// SM2

// Generate a secret key
use libsm::sm2::encrypt::*;
use libsm::sm2::signature::SigCtx;
use rustc_serialize::hex::FromHex;
use rustc_serialize::hex::ToHex;
#[no_mangle]

pub unsafe extern "C" fn Java_org_definedim_crypto_RustSM2Crypto_newSecretKey<'a>(
    env: JNIEnv<'a>,
    _class: JClass<'a>,
) -> JString<'a> {
    let ctx = SigCtx::new();
    let sk = ctx.new_keypair().unwrap().1;
    let sk_raw = ctx.serialize_seckey(&sk).unwrap();
    return env.new_string(sk_raw.as_slice().to_hex()).unwrap();
}

// Generate a public key from a secret key
#[no_mangle]
pub unsafe extern "C" fn Java_org_definedim_crypto_RustSM2Crypto_genPublicKey<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass<'a>,
    secret_key_java: JString<'a>,
) -> JString<'a> {
    let ctx = SigCtx::new();
    let secret_key = ctx
        .load_seckey(
            &env.get_string(&secret_key_java)
                .unwrap()
                .to_str()
                .unwrap()
                .to_string()
                .from_hex() // Hex to bytes
                .unwrap(),
        )
        .unwrap();
    let public_key = ctx.pk_from_sk(&secret_key).unwrap();
    let pk_raw: Vec<u8> = ctx.serialize_pubkey(&public_key, true).unwrap();
    return env.new_string(pk_raw.as_slice().to_hex()).unwrap();
}

// encrypt a string
#[no_mangle]
pub unsafe extern "C" fn Java_org_definedim_crypto_RustSM2Crypto_encryptString<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass<'a>,
    public_key_java: JString<'a>,
    raw_data: JString<'a>,
) -> JString<'a> {
    let ctx = SigCtx::new();
    let public_key = ctx
        .load_pubkey(
            &env.get_string(&public_key_java)
                .unwrap()
                .to_str()
                .unwrap()
                .to_string()
                .from_hex() // Hex to bytes
                .unwrap(),
        )
        .unwrap();
    let data = env
        .get_string(&raw_data)
        .unwrap()
        .to_str()
        .unwrap()
        .to_string();
    let encrypt_ctx = EncryptCtx::new(data.len(), public_key);
    let encrypted_data: Vec<u8> = encrypt_ctx.encrypt(data.as_bytes()).unwrap();
    return env.new_string(encrypted_data.as_slice().to_hex()).unwrap();
}

// decrypt a string
#[no_mangle]
pub unsafe extern "C" fn Java_org_definedim_crypto_RustSM2Crypto_decryptString<'a>(
    mut env: JNIEnv<'a>,
    _class: JClass<'a>,
    secret_key_java: JString<'a>,
    raw_data: JString<'a>,
) -> JString<'a> {
    let ctx = SigCtx::new();
    let secret_key = ctx
        .load_seckey(
            &env.get_string(&secret_key_java)
                .unwrap()
                .to_str()
                .unwrap()
                .to_string()
                .from_hex() // Hex to bytes
                .unwrap(),
        )
        .unwrap();
    let data = env
        .get_string(&raw_data)
        .unwrap()
        .to_str()
        .unwrap()
        .to_string()
        .from_hex() // Hex to bytes
        .unwrap();
    let decrypt_ctx = DecryptCtx::new(data.len() - 97, secret_key);
    let decrypted_data: Vec<u8> = decrypt_ctx.decrypt(&data).unwrap();
    return env
        .new_string(String::from_utf8(decrypted_data).unwrap())
        .unwrap();
}
