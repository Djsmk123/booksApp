package com.example.bookapp.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.bookapp.R
import com.example.bookapp.components.ItemBookList
import com.example.bookapp.components.TextInputField
import com.example.bookapp.model.BookItem
import com.example.bookapp.navigation.MainActions
import com.example.bookapp.utils.ViewState
import com.example.bookapp.viewmodel.MainViewModel


@ExperimentalComposeUiApi
@Composable
fun BookListScreen(viewModel: MainViewModel, actions: MainActions) {

    when (val result = viewModel.books.value) {
        ViewState.Loading -> Text(text = "Loading")
        is ViewState.Error -> Text(text = "Error found: ${result.exception}")
        is ViewState.Success -> {
            BookList(result.data, actions)
        }
        ViewState.Empty -> Text("No results found!")
    }
}

@ExperimentalComposeUiApi
@Composable
fun BookList(bookList: List<BookItem>, actions: MainActions) {

    val search = remember {
        mutableStateOf("")
    }

    val listState = rememberLazyListState()

    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(top = 24.dp, bottom = 24.dp),
        modifier = Modifier.background(MaterialTheme.colors.background)
    ) {
        // title
        item {
            Text(
                text = "Explore thousands of  \nbooks in go",
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Start,
                color = MaterialTheme.colors.primaryVariant,
                maxLines = 2,
                modifier = Modifier.padding(start = 16.dp, end = 24.dp, bottom = 24.dp),
            )
        }
        // search
        item {
            TextInputField(
                label = stringResource(R.string.text_search),
                value = search.value, onValueChanged = {
                    search.value = it
                })
        }
        // Search result title
        item {
            Text(
                text = "Famous books",
                style = MaterialTheme.typography.subtitle1,

                color = Color.White,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(20.dp)
            )
        }

        // All books list view
        items(bookList.filter { it.title.contains(search.value, ignoreCase = true) }) { book ->
            Log.d("books", "books are ${book.title}")
            ItemBookList(
                book.title,
                book.authors,
                book.thumbnailUrl,
                book.categories,
                onItemClick = {
                    actions.gotoBookDetails.invoke(book.isbn)

                })
        }

    }
}
