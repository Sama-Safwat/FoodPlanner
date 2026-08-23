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
            val countryCode = getCountryCode(countryName)
            return countryCodeToFlagEmoji(countryCode)
        }

        private fun getCountryCode(countryName: String): String {
            return when (countryName.lowercase()) {
                "american" -> "US"
                "british" -> "GB"
                "canadian" -> "CA"
                "chinese" -> "CN"
                "croatian" -> "HR"
                "dutch" -> "NL"
                "egyptian" -> "EG"
                "filipino" -> "PH"
                "french" -> "FR"
                "greek" -> "GR"
                "indian" -> "IN"
                "irish" -> "IE"
                "italian" -> "IT"
                "jamaican" -> "JM"
                "japanese" -> "JP"
                "kenyan" -> "KE"
                "malaysian" -> "MY"
                "mexican" -> "MX"
                "moroccan" -> "MA"
                "polish" -> "PL"
                "portuguese" -> "PT"
                "russian" -> "RU"
                "spanish" -> "ES"
                "thai" -> "TH"
                "tunisian" -> "TN"
                "turkish" -> "TR"
                "vietnamese" -> "VN"
                else -> "UN"
            }
        }

        private fun countryCodeToFlagEmoji(code: String): String {
            val firstLetter = code[0].uppercaseChar()
            val secondLetter = code[1].uppercaseChar()
            val firstChar = 0x1F1E6 + (firstLetter.code - 'A'.code)
            val secondChar = 0x1F1E6 + (secondLetter.code - 'A'.code)
            return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
        }
    }
}