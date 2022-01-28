package io.sixtant;

public interface IStarkSigner {

    void init(IAssetMetaData assetMetaData);

    String sign(IStarkSignatureArg arg);
}
