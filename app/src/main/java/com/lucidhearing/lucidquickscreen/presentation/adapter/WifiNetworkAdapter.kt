package com.lucidhearing.lucidquickscreen.presentation.adapter

import android.media.MediaCodecInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lucidhearing.lucidquickscreen.databinding.WifiNetworkListItemBinding
import com.lucidhearing.lucidquickscreen.utils.AppUtils

class WifiNetworkAdapter:RecyclerView.Adapter<WifiNetworkAdapter.WifiNetworkViewHolder>() {

    private var onResponseClickListener:((WifiNetwork) -> Unit)? = null
    var wifiNetworks:List<WifiNetwork> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WifiNetworkViewHolder {
        val binding = WifiNetworkListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WifiNetworkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WifiNetworkViewHolder, position: Int) {
        holder.bind(wifiNetworks[position])
    }

    override fun getItemCount(): Int {
       return wifiNetworks.size
    }

    inner class WifiNetworkViewHolder(
        val binding:WifiNetworkListItemBinding
    ):RecyclerView.ViewHolder(binding.root){
        fun bind(wifiNetwork:WifiNetwork){
            binding.buttonWifiName.text = AppUtils.getNetworkSSID(wifiNetwork.ssid)
            binding.imgIsConnected.visibility = if(wifiNetwork.isConnected) View.VISIBLE else View.GONE
            binding.buttonWifiName.setOnClickListener {
                onResponseClickListener?.let {
                    it(wifiNetwork)
                }
            }
        }
    }

    fun setOnItemClickListener(listener :((WifiNetwork)-> Unit)){
        onResponseClickListener = listener
    }
}

data class WifiNetwork(
    var ssid:String,
    var bssid:String,
    var password:String? = null,
    var capabilities: String,
    var isConnected:Boolean=false
)