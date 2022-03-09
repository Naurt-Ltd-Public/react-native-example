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
  NaurtIosInterface
} from './NaurtIosInterface';
import { NaurtPoint } from './NaurtInterfaces';

const NaurtIosComponent = () => {
  const NaurtIosModule =
    NativeModules.RNNaurtIosModule as NaurtIosInterface;
  let naurtEventEmitter: NativeEventEmitter;

  const [naurtDisplay, setNaurtDisplay] = useState(<></>);
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
    // const naurtAndroidConstants = NaurtAndroidModule.getConstants();
    // naurtEventEmitter = new NativeEventEmitter(NaurtAndroidModule);

    // console.log(naurtAndroidConstants);

    // PermissionsAndroid.requestMultiple([
    //   'android.permission.ACCESS_COARSE_LOCATION',
    //   'android.permission.ACCESS_FINE_LOCATION',
    //   'android.permission.WRITE_EXTERNAL_STORAGE',
    // ])
    //   .then(result => {
    //     let granted =
    //       result['android.permission.ACCESS_COARSE_LOCATION'] === 'granted' &&
    //       result['android.permission.ACCESS_FINE_LOCATION'] === 'granted' &&
    //       result['android.permission.WRITE_EXTERNAL_STORAGE'] === 'granted';

    //     console.log('Permissions: ' + granted);

    //     if (granted) {
    //       naurtEventEmitter.addListener(
    //         'NAURT_IS_INITIALISED',
    //         (event: NaurtInisialiedEvent) => {
    //           console.log('NAURT_IS_INITIALISED: ' + event.isInitialised);
    //           setNaurtIsInitialised(event.isInitialised);
    //         },
    //       );

        //   naurtEventEmitter.addListener(
        //     'NAURT_IS_VALIDATED',
        //     (event: NaurtValidatedEvent) => {
        //       console.log('NAURT_IS_VALIDATED: ' + event.isValidated);
        //       setNaurtIsValidated(event.isValidated);

        //       if (!naurtIsRunning) {
        //         // Simple Start here, post initialisation
        //         NaurtAndroidModule.startNaurt();
        //       }
        //     },
        //   );

        //   naurtEventEmitter.addListener(
        //     'NAURT_IS_RUNNING',
        //     (event: NaurtRunningEvent) => {
        //       console.log('NAURT_IS_RUNNING: ' + event.isRunning);
        //       setNaurtIsRunning(event.isRunning);
        //     },
        //   );

        //   naurtEventEmitter.addListener(
        //     'NAURT_NEW_POINT',
        //     (event: NaurtPointEvent) => {
        //       console.log(
        //         `NAURT_NEW_POINT: [${event.latitude}, ${event.longitude}], ${event.timestamp}`,
        //       );
        //       setNaurtPoint({
        //         latitude: event.latitude,
        //         longitude: event.longitude,
        //         timestamp: event.timestamp,
        //       });
        //     },
        //   );

          // This doesn't seem to call the method, or at least not print logs
          NaurtIosModule.initialiseNaurt(
            '4b4d91b4-db2f-4104-922d-e0c94d9fa472-3c0ecfd8-c29a-498f-8d81-8bc58b318698',
            6
          );

          if (!naurtIsRunning) {
            // Simple Start here, post initialisation
            NaurtIosModule.startNaurt();
          }
        // }
    //   })
    //   .catch(err => {
    //     console.error(err);
    //   });

    return () => {
      NaurtIosModule.stopNaurt();
    };
  }, []);

  useEffect(() => {
    setNaurtDisplay(
      <Text>{`${naurtPoint.timestamp}: Lat: ${naurtPoint.latitude}, Lon: ${naurtPoint.longitude}`}</Text>,
    );

    return () => {};
  }, [naurtPoint]);

  return (
    <View
      style={{
        backgroundColor: isDarkMode ? Colors.black : Colors.white,
      }}>
      {naurtDisplay}
    </View>
  );
};

export default NaurtIosComponent;
