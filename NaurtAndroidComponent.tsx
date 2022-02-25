import React, { useEffect } from "react"
import NaurtAndroidModule from "./NaurtModule"

const NaurtAndroidComponent = () => {
    const naurtAndroidConstants = NaurtAndroidModule.getConstants();

    useEffect(() => {
        let hasPermission = NaurtAndroidModule.checkPermissions();

        if (hasPermission) {
            NaurtAndroidModule.initialiseNaurt('<YOUR_API_KEY_HERE>')
        }

        // Simple Start here, post initialisation
        NaurtAndroidModule.startNaurt((eid, lat, lon, time) => {
            if (eid === Number(naurtAndroidConstants.get("NAURT_EVENT_ID"))) {
                console.log(`New Naurt Location!: ${lat}, ${lon} at ${time}`)
            }
        })
    
        return () => {
            NaurtAndroidModule.stopNaurt()
        }
    }, [])

    return (
        <></>
    )
}

export default NaurtAndroidComponent