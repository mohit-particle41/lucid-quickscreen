package com.lucidhearing.lucidquickscreen.utils

import com.google.common.truth.Truth.assertThat
import com.lucidhearing.lucidquickscreen.utils.constants.AppConstant
import org.junit.Test

class AppUtilsTest{

    @Test
    fun isNameValid_validName_returnsCorrectResult(){
        val inputName:String = "John"
        val result = AppUtils.isNameValid(inputName)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun isNameValid_invalidName_returnsIncorrectResult(){
        val inputName:String = "J"
        val result = AppUtils.isNameValid(inputName)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun isEmailValid_validEmailGiven_returnsCorrectResult(){
        val inputEmail:String = "test@example.com"
        val result = AppUtils.isEmailValid(inputEmail)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun isEmailValid_validEmailGivenSubDomain_returnsCorrectResult(){
        val inputEmail:String = "test@example.co.in"
        val result = AppUtils.isEmailValid(inputEmail)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun isEmailValid_emptyStringGiven_returnsIncorrectResult(){
        val inputEmail:String = ""
        val result = AppUtils.isEmailValid(inputEmail)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun isEmailValid_invalidEmailGivenNoUsername_returnsIncorrectResult(){
        val inputEmail:String = "@example.com"
        val result = AppUtils.isEmailValid(inputEmail)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun isEmailValid_invalidEmailGivenNoDomain_returnsIncorrectResult(){
        val inputEmail:String = "test@.com"
        val result = AppUtils.isEmailValid(inputEmail)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun isZipValid_validZipcode_returnsCorrectResult(){
        val inputZip:String = "75001"
        val result = AppUtils.isZipValid(inputZip)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun isZipValid_invalidZipcode_returnsIncorrectResult(){
        val inputZip:String = "75"
        val result = AppUtils.isZipValid(inputZip)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun isPhoneValid_validPhone_returnsCorrectResult(){
        val inputPhone:String = "7607567568"
        val result = AppUtils.isPhoneValid(inputPhone)
        assertThat(result).isEqualTo(true)
    }

    @Test
    fun isPhoneValid_invalidPhone_returnsIncorrectResult(){
        val inputPhone:String = "ABCD760"
        val result = AppUtils.isPhoneValid(inputPhone)
        assertThat(result).isEqualTo(false)
    }

    @Test
    fun isNetworkSecured_secureNetworkCapabilities_returnsTrue(){
        val networkCapabilities = "[WPA2-PSK-CCMP][WPS][ESS]"
        val result = AppUtils.isNetworkSecured(networkCapabilities)
        assertThat(result).isTrue()
    }

    @Test
    fun isNetworkSecured_insecureNetworkCapabilities_returnsFalse(){
        val networkCapabilities = "[ESS]"
        val result = AppUtils.isNetworkSecured(networkCapabilities)
        assertThat(result).isFalse()
    }

    @Test
    fun isWifiPasswordValid_validPassword_returnsTrue(){
        val wifiPassword = "123456789"
        val result = AppUtils.isWifiPasswordValid(wifiPassword)
        assertThat(result).isTrue()
    }

    @Test
    fun isWifiPasswordValid_invalidPassword_returnsFalse(){
        val wifiPassword = "1234"
        val result = AppUtils.isWifiPasswordValid(wifiPassword)
        assertThat(result).isFalse()
    }

    @Test
    fun getNetworkSSID_validNetworkSSID_returnsProvidedSSID(){
        val ssid = "mynetwork"
        val result = AppUtils.getNetworkSSID(ssid)
        assertThat(result).isEqualTo(ssid)
    }

    @Test
    fun getNetworkSSID_unnamedNetworkSSID_returnsUnnamedSSIDString(){
        val ssid = ""
        val result = AppUtils.getNetworkSSID(ssid)
        assertThat(result).isEqualTo(AppConstant.UNNAMED_NETWORK)
    }

}