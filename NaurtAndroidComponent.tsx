import React, {useEffect, useState} from 'react';
import {
  NativeEventEmitter,
  PermissionsAndroid,
  Text,
  useColorScheme,
  View,
} from 'react-native';
import {Colors} from 'react-native/Libraries/NewAppScreen';
import {NativeModules} from 'react-native';
import {
  NaurtAndroidInterface,
  NaurtInisialiedEvent,
  NaurtPoint,
  NaurtPointEvent,
  NaurtRunningEvent,
  NaurtValidatedEvent,
} from './NaurtAndroidInterface';

const NaurtAndroidComponent = () => {
  const NaurtAndroidModule =
    NativeModules.NaurtAndroidModule as NaurtAndroidInterface;
  let naurtEventEmitter: NativeEventEmitter;

  const [naurtDisplay, setNaurtDisplay] = useState(<></>);
  const [naurtPos, setNaurtPos] = useState([NaN, NaN, NaN]);
  //   const [hasPermission, setHasPermission] = useState(false);

  const [naurtIsInitialised, setNaurtIsInitialised] = useState(false);
  const [naurtIsValidated, setNaurtIsValidated] = useState(false);
  const [naurtIsRunning, setNaurtIsRunning] = useState(false);
  const [naurtPoint, setNaurtPoint] = useState({
    latitude: 0.0,
    longitude: 0.0,
    timestamp: '',
  } as NaurtPoint);

  const isDarkMode = useColorScheme() === 'dark';

  useEffect(() => {
    const naurtAndroidConstants = NaurtAndroidModule.getConstants();
    naurtEventEmitter = new NativeEventEmitter(NaurtAndroidModule as any);

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

        console.log('Permissions: ' + granted);

        if (granted) {
          NaurtAndroidModule.addListener(
            'NAURT_IS_INITIALISED',
            (event: NaurtInisialiedEvent) => {
              console.log('NAURT_IS_INITIALISED: ' + event.isInitialised);
              setNaurtIsInitialised(event.isInitialised);
            },
          );

          NaurtAndroidModule.addListener(
            'NAURT_IS_VALIDATED',
            (event: NaurtValidatedEvent) => {
              console.log('NAURT_IS_VALIDATED: ' + event.isValidated);
              setNaurtIsValidated(event.isValidated);
            },
          );

          NaurtAndroidModule.addListener(
            'NAURT_IS_RUNNING',
            (event: NaurtRunningEvent) => {
              console.log('NAURT_IS_RUNNING: ' + event.isRunning);
              setNaurtIsRunning(event.isRunning);
            },
          );

          NaurtAndroidModule.addListener(
            'NAURT_NEW_POINT',
            (event: NaurtPointEvent) => {
              console.log(
                `NAURT_NEW_POINT: [${event.latitude}, ${event.longitude}], ${event.timestamp}`,
              );
              setNaurtPoint({
                latitude: event.latitude,
                longitude: event.longitude,
                timestamp: event.timestamp,
              });
            },
          );

          // This doesn't seem to call the method, or at least not print logs
          NaurtAndroidModule.initialiseNaurt(
            '4b4d91b4-db2f-4104-922d-e0c94d9fa472-3c0ecfd8-c29a-498f-8d81-8bc58b318698',
          );

          // Simple Start here, post initialisation
          NaurtAndroidModule.startNaurt();
        }
      })
      .catch(err => {
        console.error(err);
      });

    return () => {
      NaurtAndroidModule.stopNaurt();
    };
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
