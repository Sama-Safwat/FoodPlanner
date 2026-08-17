package com.example.foodplanner.ui.countries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodplanner.data.model.Area
import com.example.foodplanner.databinding.ItemCountryBinding

class CountriesAdapter(
    private val onCountryClick: (Area) -> Unit
) : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {

    private var countries: List<Area> = emptyList()

    fun submitList(list: List<Area>) {
        countries = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val binding = ItemCountryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CountryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(countries[position])
    }

    override fun getItemCount(): Int = countries.size

    inner class CountryViewHolder(
        private val binding: ItemCountryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(country: Area) {
            val countryName = country.strArea ?: "Unknown"
            binding.countryName.text = countryName
            binding.countryFlag.text = getCountryFlag(countryName)

            binding.root.setOnClickListener {
                onCountryClick(country)
            }
        }

        private fun getCountryFlag(countryName: String): String {
            return when (countryName.lowercase()) {
                "american" -> "🇺🇸"
                "british" -> "🇬🇧"
                "canadian" -> "🇨🇦"
                "chinese" -> "🇨🇳"
                "croatian" -> "🇭🇷"
                "dutch" -> "🇳🇱"
                "egyptian" -> "🇪🇬"
                "filipino" -> "🇵🇭"
                "french" -> "🇫🇷"
                "greek" -> "🇬🇷"
                "indian" -> "🇮🇳"
                "irish" -> "🇮🇪"
                "italian" -> "🇮🇹"
                "jamaican" -> "🇯🇲"
                "japanese" -> "🇯🇵"
                "kenyan" -> "🇰🇪"
                "malaysian" -> "🇲🇾"
                "mexican" -> "🇲🇽"
                "moroccan" -> "🇲🇦"
                "polish" -> "🇵🇱"
                "portuguese" -> "🇵🇹"
                "russian" -> "🇷🇺"
                "spanish" -> "🇪🇸"
                "thai" -> "🇹🇭"
                "tunisian" -> "🇹🇳"
                "turkish" -> "🇹🇷"
                "vietnamese" -> "🇻🇳"
                else -> "🌍"
            }
        }
    }
}