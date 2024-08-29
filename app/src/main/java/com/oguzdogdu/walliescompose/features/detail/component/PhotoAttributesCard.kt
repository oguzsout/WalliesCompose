package com.oguzdogdu.walliescompose.features.detail.component

import TooltipPopup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.ui.theme.medium
import com.oguzdogdu.walliescompose.ui.theme.regular

@Composable
fun PhotoAttributesCard(pairsOfPhotoAttributes: List<Pair<Int, AnnotatedString>>, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ), shape = RoundedCornerShape(
            16.dp
        ), modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .heightIn(min = 120.dp, max = 360.dp)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            state = rememberLazyGridState(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            items(pairsOfPhotoAttributes) {(resId, stringBuilder) ->
                Column(
                    modifier = Modifier.padding(vertical = 8.dp),verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Center
                    ) {
                        if (resId == R.string.dimensions) {
                            Row(modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)) {
                                TooltipPopup(
                                    triggeredView = { modifier ->
                                        Icon(
                                            modifier = modifier.size(20.dp),
                                            painter = painterResource(id = R.drawable.info),
                                            contentDescription = "TooltipPopup",
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        )
                                    },
                                    tooltipContent = {
                                        Row(modifier = Modifier.padding(horizontal = 8.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                                            Icon(
                                                modifier = modifier.size(20.dp),
                                                painter = painterResource(id = R.drawable.baseline_warning_24),
                                                contentDescription = "TooltipPopup",
                                                tint = Color.Yellow,
                                            )
                                            Text(
                                                modifier = Modifier
                                                    .padding(12.dp),
                                                text = stringResource(R.string.text_dimension_info),
                                                style = TextStyle(
                                                    fontSize = 16.sp,
                                                    fontFamily = regular,
                                                    textAlign = TextAlign.Start
                                                ),
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            )
                                        }
                                    }
                                )
                            }
                        }
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(id = resId),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 14.sp,
                                fontFamily = medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = stringBuilder,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 14.sp,
                                fontFamily = regular,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
