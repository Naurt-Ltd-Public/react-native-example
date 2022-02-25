import React, {useEffect, useState} from 'react';
import {Text, useColorScheme, View} from 'react-native';
import { Colors } from 'react-native/Libraries/NewAppScreen';
import { NativeModules } from 'react-native';

const NaurtAndroidComponent = () => {
  const NaurtAndroidModule = NativeModules.NaurtAndroidModule;
    
  const [naurtDisplay, setNaurtDisplay] = useState(<></>)
  const [naurtPos, setNaurtPos] = useState([NaN, NaN, NaN])
  const [hasPermission, setHasPermission] = useState(false)

  const isDarkMode = useColorScheme() === 'dark';

  useEffect(() => {
        setTimeout(() => {
            console.log(NaurtAndroidModule)

            const naurtAndroidConstants = NaurtAndroidModule.getConstants();

            let perms = NaurtAndroidModule.checkPermissions();
            setHasPermission(perms)
            console.log("Has Permission: " + perms)

            if (hasPermission) {
            NaurtAndroidModule.initialiseNaurt(
                '4b4d91b4-db2f-4104-922d-e0c94d9fa472-3c0ecfd8-c29a-498f-8d81-8bc58b318698',
            );
            }

            const callback = (eid: number, lat: number, lon: number, time: number) => {
                if (eid === Number(naurtAndroidConstants.get('NAURT_EVENT_ID'))) {
                console.log(`New Naurt Location!: ${lat}, ${lon} at ${time}`);
                setNaurtPos([time, lat, lon])
                }
            }

            // Simple Start here, post initialisation
            NaurtAndroidModule.startNaurt(callback);

            return () => {
            NaurtAndroidModule.stopNaurt();
            };
        }, 200)
  }, []);

  useEffect(() => {
    setNaurtDisplay(<Text>{`${naurtPos[0]}: Lat: ${naurtPos[1]}, Lon: ${naurtPos[2]}`}</Text>)
  
    return () => {setNaurtPos([NaN, NaN, NaN])}
  }, [naurtPos])
  

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
