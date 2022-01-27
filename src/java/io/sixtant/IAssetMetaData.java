package io.sixtant;

import java.math.BigInteger;
import java.util.Map;

public interface IAssetMetaData {

    CharSequence collateralAsset();

    Map<String, String> syntheticAsset();

    Map<String, BigInteger> assetId();

    Map<String, BigInteger> lots();
}
