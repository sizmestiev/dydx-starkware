package io.sixtant;

public interface IStarkSigner {

    void init(IAssetMetaData assetMetaData, boolean testnet);

    String sign(IStarkSignatureArg arg);
}
