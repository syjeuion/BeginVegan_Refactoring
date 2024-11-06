package com.beginvegan.presentation.view.home.veganTest.view.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.beginvegan.presentation.R
import com.beginvegan.presentation.compose.style.CaptionRegular

@Composable
fun CustomRadioButton(
    selected:Boolean,
    description:String,
    isMilk: Boolean,isEgg: Boolean,isFish: Boolean,isChicken: Boolean,isMeat: Boolean,
    onClick: ()->Unit
){
    val checkedIcon = painterResource(R.drawable.ic_radio_checked)
    val uncheckedIcon = painterResource(R.drawable.ic_radio_unchecked)
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = if(selected) checkedIcon else uncheckedIcon,
            contentDescription = null,
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .clickable { onClick() }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            drawVeganIllus(isMilk,isEgg, isFish, isChicken, isMeat)
            if(selected){
                Text(
                    text = description,
                    style = CaptionRegular()
                )
            }
        }
    }
}

@Composable
private fun drawVeganIllus(isMilk:Boolean, isEgg:Boolean, isFish:Boolean, isChicken:Boolean, isMeat:Boolean){
    val illusMilk = if (isMilk) R.drawable.illus_vegan_level_milk_filled else R.drawable.illus_vegan_level_milk_empty
    val illusEgg = if (isEgg) R.drawable.illus_vegan_level_egg_filled else R.drawable.illus_vegan_level_egg_empty
    val illusFish = if (isFish) R.drawable.illus_vegan_level_fish_filled else R.drawable.illus_vegan_level_fish_empty
    val illusChicken = if (isChicken) R.drawable.illus_vegan_level_chicken_filled else R.drawable.illus_vegan_level_chicken_empty
    val illusMeat = if (isMeat) R.drawable.illus_vegan_level_meat_filled else R.drawable.illus_vegan_level_meat_empty

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.illus_vegan_level_salad_filled),
            contentDescription = "Illustration 2",
            modifier = Modifier.width(48.dp).height(38.dp)
        )
        Image(
            painter = painterResource(id = illusMilk),
            contentDescription = "Illustration 2",
            modifier = Modifier.width(40.dp).height(40.dp)
        )
        Image(
            painter = painterResource(id = illusEgg),
            contentDescription = "Illustration 2",
            modifier = Modifier.width(42.dp).height(36.dp)
        )
        Image(
            painter = painterResource(id = illusFish),
            contentDescription = "Illustration 2",
            modifier = Modifier.width(40.dp).height(42.dp)
        )
        Image(
            painter = painterResource(id = illusChicken),
            contentDescription = "Illustration 2",
            modifier = Modifier.width(46.dp).height(26.dp)
        )
        Image(
            painter = painterResource(id = illusMeat),
            contentDescription = "Illustration 2",
            modifier = Modifier.width(38.dp).height(28.dp)
        )
    }
}