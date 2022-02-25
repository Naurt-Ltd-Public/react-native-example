import { NativeModules } from 'react-native';
const { NaurtModule: NaurtAndroidModule } = NativeModules;

interface NaurtAndroidInterface {
    getName(): String;
    getConstants(): Map<String, String>;
    initialiseNaurt(apiKey: String): void;
    resumeNaurt(): void;
    pauseNaurt(): void;
    startNaurt(callback: (eventId: Number, latitude: Number, longitude: Number, timestamp: Number) => void): void;
    stopNaurt(): void;
    checkPermissions(): boolean;
}

export default NaurtAndroidModule as NaurtAndroidInterface;