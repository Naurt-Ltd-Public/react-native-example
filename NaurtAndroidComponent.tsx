import React, {useEffect, useState} from 'react';
import {PermissionsAndroid, Text, useColorScheme, View} from 'react-native';
import {Colors} from 'react-native/Libraries/NewAppScreen';
import {NativeModules} from 'react-native';
import {NaurtAndroidInterface} from './NaurtAndroidInterface';

const NaurtAndroidComponent = () => {
  const NaurtAndroidModule =
    NativeModules.NaurtAndroidModule as NaurtAndroidInterface;

  const [naurtDisplay, setNaurtDisplay] = useState(<></>);
  const [naurtPos, setNaurtPos] = useState([NaN, NaN, NaN]);
  //   const [hasPermission, setHasPermission] = useState(false);

  const isDarkMode = useColorScheme() === 'dark';

  useEffect(() => {
    const naurtAndroidConstants = NaurtAndroidModule.getConstants();
    console.log(naurtAndroidConstants);

    PermissionsAndroid.requestMultiple([
      'android.permission.ACCESS_COARSE_LOCATION',
      'android.permission.ACCESS_FINE_LOCATION',
      'android.permission.WRITE_EXTERNAL_STORAGE',
    ])
      .then(result => {
        let granted =
          result['android.permission.ACCESS_COARSE_LOCATION'] === 'granted' &&
          result['android.permission.ACCESS_FINE_LOCATION'] === 'granted' &&
          result['android.permission.WRITE_EXTERNAL_STORAGE'] === 'granted';

        console.log('permissions granted: ' + granted);

        // This doesn't seem to call the method, or at least not print logs
        NaurtAndroidModule.initialiseNaurt(
          '4b4d91b4-db2f-4104-922d-e0c94d9fa472-3c0ecfd8-c29a-498f-8d81-8bc58b318698',
        );
      })
      .catch(err => {
        console.error(err);
      });

    // let perms = NaurtAndroidModule.checkPermissions();
    // setHasPermission(perms);
    // console.log('Has Permission: ' + perms);

    // if (hasPermission) {
    //   NaurtAndroidModule.initialiseNaurt(
    //     '4b4d91b4-db2f-4104-922d-e0c94d9fa472-3c0ecfd8-c29a-498f-8d81-8bc58b318698',
    //   );
    // }

    // const callback = (eid: number, lat: number, lon: number, time: number) => {
    //   if (eid === Number(naurtAndroidConstants.get('NAURT_EVENT_ID'))) {
    //     console.log(`New Naurt Location!: ${lat}, ${lon} at ${time}`);
    //     setNaurtPos([time, lat, lon]);
    //   }
    // };

    // // Simple Start here, post initialisation
    // NaurtAndroidModule.startNaurt(callback);

    // return () => {
    //   NaurtAndroidModule.stopNaurt();
    // };
  }, []);

  useEffect(() => {
    setNaurtDisplay(
      <Text>{`${naurtPos[0]}: Lat: ${naurtPos[1]}, Lon: ${naurtPos[2]}`}</Text>,
    );

    return () => {
      setNaurtPos([NaN, NaN, NaN]);
    };
  }, [naurtPos]);

  return (
    <View
      style={{
        backgroundColor: isDarkMode ? Colors.black : Colors.white,
      }}>
      {naurtDisplay}
    </View>
  );
};

export default NaurtAndroidComponent;
