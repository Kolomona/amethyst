package com.vitorpamplona.amethyst.ui.navigation

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import coil.Coil
import com.vitorpamplona.amethyst.R
import com.vitorpamplona.amethyst.model.Account
import com.vitorpamplona.amethyst.model.AddressableNote
import com.vitorpamplona.amethyst.model.ConnectivityType
import com.vitorpamplona.amethyst.model.GLOBAL_FOLLOWS
import com.vitorpamplona.amethyst.model.KIND3_FOLLOWS
import com.vitorpamplona.amethyst.model.LocalCache
import com.vitorpamplona.amethyst.service.NostrAccountDataSource
import com.vitorpamplona.amethyst.service.NostrChannelDataSource
import com.vitorpamplona.amethyst.service.NostrChatroomDataSource
import com.vitorpamplona.amethyst.service.NostrChatroomListDataSource
import com.vitorpamplona.amethyst.service.NostrCommunityDataSource
import com.vitorpamplona.amethyst.service.NostrDiscoveryDataSource
import com.vitorpamplona.amethyst.service.NostrGeohashDataSource
import com.vitorpamplona.amethyst.service.NostrHashtagDataSource
import com.vitorpamplona.amethyst.service.NostrHomeDataSource
import com.vitorpamplona.amethyst.service.NostrSearchEventOrUserDataSource
import com.vitorpamplona.amethyst.service.NostrSingleChannelDataSource
import com.vitorpamplona.amethyst.service.NostrSingleEventDataSource
import com.vitorpamplona.amethyst.service.NostrSingleUserDataSource
import com.vitorpamplona.amethyst.service.NostrThreadDataSource
import com.vitorpamplona.amethyst.service.NostrUserProfileDataSource
import com.vitorpamplona.amethyst.service.NostrVideoDataSource
import com.vitorpamplona.amethyst.service.checkNotInMainThread
import com.vitorpamplona.amethyst.service.connectivitystatus.ConnectivityStatus
import com.vitorpamplona.amethyst.service.relays.Client
import com.vitorpamplona.amethyst.service.relays.RelayPool
import com.vitorpamplona.amethyst.ui.components.RobohashAsyncImageProxy
import com.vitorpamplona.amethyst.ui.note.AmethystIcon
import com.vitorpamplona.amethyst.ui.note.ArrowBackIcon
import com.vitorpamplona.amethyst.ui.note.ClickableUserPicture
import com.vitorpamplona.amethyst.ui.note.LoadAddressableNote
import com.vitorpamplona.amethyst.ui.note.LoadChannel
import com.vitorpamplona.amethyst.ui.note.LoadCityName
import com.vitorpamplona.amethyst.ui.note.LoadUser
import com.vitorpamplona.amethyst.ui.note.LongCommunityHeader
import com.vitorpamplona.amethyst.ui.note.NonClickableUserPictures
import com.vitorpamplona.amethyst.ui.note.SearchIcon
import com.vitorpamplona.amethyst.ui.note.ShortCommunityHeader
import com.vitorpamplona.amethyst.ui.note.UserCompose
import com.vitorpamplona.amethyst.ui.note.UsernameDisplay
import com.vitorpamplona.amethyst.ui.screen.equalImmutableLists
import com.vitorpamplona.amethyst.ui.screen.loggedIn.AccountViewModel
import com.vitorpamplona.amethyst.ui.screen.loggedIn.DislayGeoTagHeader
import com.vitorpamplona.amethyst.ui.screen.loggedIn.GeoHashActionOptions
import com.vitorpamplona.amethyst.ui.screen.loggedIn.HashtagActionOptions
import com.vitorpamplona.amethyst.ui.screen.loggedIn.LoadRoom
import com.vitorpamplona.amethyst.ui.screen.loggedIn.LoadRoomByAuthor
import com.vitorpamplona.amethyst.ui.screen.loggedIn.LongChannelHeader
import com.vitorpamplona.amethyst.ui.screen.loggedIn.LongRoomHeader
import com.vitorpamplona.amethyst.ui.screen.loggedIn.RoomNameOnlyDisplay
import com.vitorpamplona.amethyst.ui.screen.loggedIn.ShortChannelHeader
import com.vitorpamplona.amethyst.ui.screen.loggedIn.SpinnerSelectionDialog
import com.vitorpamplona.amethyst.ui.theme.BottomTopHeight
import com.vitorpamplona.amethyst.ui.theme.DividerThickness
import com.vitorpamplona.amethyst.ui.theme.DoubleHorzSpacer
import com.vitorpamplona.amethyst.ui.theme.HalfVertSpacer
import com.vitorpamplona.amethyst.ui.theme.HeaderPictureModifier
import com.vitorpamplona.amethyst.ui.theme.Size22Modifier
import com.vitorpamplona.amethyst.ui.theme.Size34dp
import com.vitorpamplona.amethyst.ui.theme.Size40dp
import com.vitorpamplona.amethyst.ui.theme.placeholderText
import com.vitorpamplona.quartz.events.ChatroomKey
import com.vitorpamplona.quartz.events.ContactListEvent
import com.vitorpamplona.quartz.events.PeopleListEvent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Composable
fun AppTopBar(
    followLists: FollowListViewModel,
    navEntryState: State<NavBackStackEntry?>,
    drawerState: DrawerState,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
    navPopBack: () -> Unit
) {
    val currentRoute by remember(navEntryState.value) {
        derivedStateOf {
            navEntryState.value?.destination?.route?.substringBefore("?")
        }
    }

    val id by remember(navEntryState.value) {
        derivedStateOf {
            navEntryState.value?.arguments?.getString("id")
        }
    }

    RenderTopRouteBar(currentRoute, id, followLists, drawerState, accountViewModel, nav, navPopBack)
}

@Composable
private fun RenderTopRouteBar(
    currentRoute: String?,
    id: String?,
    followLists: FollowListViewModel,
    drawerState: DrawerState,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
    navPopBack: () -> Unit
) {
    when (currentRoute) {
        Route.Home.base -> HomeTopBar(followLists, drawerState, accountViewModel, nav)
        Route.Video.base -> StoriesTopBar(followLists, drawerState, accountViewModel, nav)
        Route.Discover.base -> DiscoveryTopBar(followLists, drawerState, accountViewModel, nav)
        Route.Notification.base -> NotificationTopBar(followLists, drawerState, accountViewModel, nav)
        Route.Settings.base -> TopBarWithBackButton(stringResource(id = R.string.application_preferences), navPopBack)
        else -> {
            if (id != null) {
                when (currentRoute) {
                    Route.Channel.base -> ChannelTopBar(id, accountViewModel, nav, navPopBack)
                    Route.RoomByAuthor.base -> RoomByAuthorTopBar(id, accountViewModel, nav, navPopBack)
                    Route.Room.base -> RoomTopBar(id, accountViewModel, nav, navPopBack)
                    Route.Community.base -> CommunityTopBar(id, accountViewModel, nav, navPopBack)
                    Route.Hashtag.base -> HashTagTopBar(id, accountViewModel, navPopBack)
                    Route.Geohash.base -> GeoHashTopBar(id, accountViewModel, navPopBack)
                    else -> MainTopBar(drawerState, accountViewModel, nav)
                }
            } else {
                MainTopBar(drawerState, accountViewModel, nav)
            }
        }
    }
}

@Composable
private fun GeoHashTopBar(
    tag: String,
    accountViewModel: AccountViewModel,
    navPopBack: () -> Unit
) {
    FlexibleTopBarWithBackButton(
        title = {
            DislayGeoTagHeader(tag, remember { Modifier.weight(1f) })
            GeoHashActionOptions(tag, accountViewModel)
        },
        popBack = navPopBack
    )
}

@Composable
private fun HashTagTopBar(
    tag: String,
    accountViewModel: AccountViewModel,
    navPopBack: () -> Unit
) {
    FlexibleTopBarWithBackButton(
        title = {
            Text(
                remember(tag) { "#$tag" },
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            HashtagActionOptions(tag, accountViewModel)
        },
        popBack = navPopBack
    )
}

@Composable
private fun CommunityTopBar(
    id: String,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
    navPopBack: () -> Unit
) {
    LoadAddressableNote(aTagHex = id, accountViewModel) { baseNote ->
        if (baseNote != null) {
            FlexibleTopBarWithBackButton(
                title = {
                    ShortCommunityHeader(baseNote, fontWeight = FontWeight.Medium, accountViewModel, nav)
                },
                extendableRow = {
                    Column(Modifier.verticalScroll(rememberScrollState())) {
                        LongCommunityHeader(baseNote = baseNote, accountViewModel = accountViewModel, nav = nav)
                    }
                },
                popBack = navPopBack
            )
        } else {
            Spacer(BottomTopHeight)
        }
    }
}

@Composable
private fun RoomTopBar(
    id: String,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
    navPopBack: () -> Unit
) {
    LoadRoom(roomId = id, accountViewModel) { room ->
        if (room != null) {
            RenderRoomTopBar(room, accountViewModel, nav, navPopBack)
        } else {
            Spacer(BottomTopHeight)
        }
    }
}

@Composable
private fun RoomByAuthorTopBar(
    id: String,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
    navPopBack: () -> Unit
) {
    LoadRoomByAuthor(authorPubKeyHex = id, accountViewModel) { room ->
        if (room != null) {
            RenderRoomTopBar(room, accountViewModel, nav, navPopBack)
        } else {
            Spacer(BottomTopHeight)
        }
    }
}

@Composable
private fun RenderRoomTopBar(
    room: ChatroomKey,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
    navPopBack: () -> Unit
) {
    if (room.users.size == 1) {
        FlexibleTopBarWithBackButton(
            title = {
                LoadUser(baseUserHex = room.users.first(), accountViewModel) { baseUser ->
                    if (baseUser != null) {
                        ClickableUserPicture(
                            baseUser = baseUser,
                            accountViewModel = accountViewModel,
                            size = Size34dp
                        )

                        Spacer(modifier = DoubleHorzSpacer)

                        UsernameDisplay(baseUser, Modifier.weight(1f), fontWeight = FontWeight.Medium)
                    }
                }
            },
            extendableRow = {
                LoadUser(baseUserHex = room.users.first(), accountViewModel) {
                    if (it != null) {
                        UserCompose(
                            baseUser = it,
                            accountViewModel = accountViewModel,
                            nav = nav
                        )
                    }
                }
            },
            popBack = navPopBack
        )
    } else {
        FlexibleTopBarWithBackButton(
            title = {
                NonClickableUserPictures(
                    users = room.users,
                    accountViewModel = accountViewModel,
                    size = Size34dp
                )

                RoomNameOnlyDisplay(
                    room,
                    Modifier
                        .padding(start = 10.dp)
                        .weight(1f),
                    fontWeight = FontWeight.Medium,
                    accountViewModel.userProfile()
                )
            },
            extendableRow = {
                LongRoomHeader(room = room, accountViewModel = accountViewModel, nav = nav)
            },
            popBack = navPopBack
        )
    }
}

@Composable
private fun ChannelTopBar(
    id: String,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
    navPopBack: () -> Unit
) {
    LoadChannel(baseChannelHex = id, accountViewModel) { baseChannel ->
        FlexibleTopBarWithBackButton(
            title = {
                ShortChannelHeader(
                    baseChannel = baseChannel,
                    accountViewModel = accountViewModel,
                    fontWeight = FontWeight.Medium,
                    nav = nav,
                    showFlag = true
                )
            },
            extendableRow = {
                LongChannelHeader(baseChannel = baseChannel, accountViewModel = accountViewModel, nav = nav)
            },
            popBack = navPopBack
        )
    }
}

@Composable
fun NoTopBar() {
}

@Composable
fun StoriesTopBar(followLists: FollowListViewModel, drawerState: DrawerState, accountViewModel: AccountViewModel, nav: (String) -> Unit) {
    GenericMainTopBar(drawerState, accountViewModel, nav) { accountViewModel ->
        val list by accountViewModel.storiesListLiveData.observeAsState(GLOBAL_FOLLOWS)

        FollowListWithRoutes(
            followListsModel = followLists,
            listName = list
        ) { listName ->
            accountViewModel.account.changeDefaultStoriesFollowList(listName.code)
        }
    }
}

@Composable
fun HomeTopBar(followLists: FollowListViewModel, drawerState: DrawerState, accountViewModel: AccountViewModel, nav: (String) -> Unit) {
    GenericMainTopBar(drawerState, accountViewModel, nav) { accountViewModel ->
        val list by accountViewModel.homeListLiveData.observeAsState(KIND3_FOLLOWS)

        FollowListWithRoutes(
            followListsModel = followLists,
            listName = list
        ) { listName ->
            if (listName.type == CodeNameType.ROUTE) {
                nav(listName.code)
            } else {
                accountViewModel.account.changeDefaultHomeFollowList(listName.code)
            }
        }
    }
}

@Composable
fun NotificationTopBar(followLists: FollowListViewModel, drawerState: DrawerState, accountViewModel: AccountViewModel, nav: (String) -> Unit) {
    GenericMainTopBar(drawerState, accountViewModel, nav) { accountViewModel ->
        val list by accountViewModel.notificationListLiveData.observeAsState(GLOBAL_FOLLOWS)

        FollowListWithoutRoutes(
            followListsModel = followLists,
            listName = list
        ) { listName ->
            accountViewModel.account.changeDefaultNotificationFollowList(listName.code)
        }
    }
}

@Composable
fun DiscoveryTopBar(followLists: FollowListViewModel, drawerState: DrawerState, accountViewModel: AccountViewModel, nav: (String) -> Unit) {
    GenericMainTopBar(drawerState, accountViewModel, nav) { accountViewModel ->
        val list by accountViewModel.discoveryListLiveData.observeAsState(GLOBAL_FOLLOWS)

        FollowListWithoutRoutes(
            followListsModel = followLists,
            listName = list
        ) { listName ->
            accountViewModel.account.changeDefaultDiscoveryFollowList(listName.code)
        }
    }
}

@Composable
fun MainTopBar(drawerState: DrawerState, accountViewModel: AccountViewModel, nav: (String) -> Unit) {
    GenericMainTopBar(drawerState, accountViewModel, nav) {
        AmethystClickableIcon()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericMainTopBar(
    drawerState: DrawerState,
    accountViewModel: AccountViewModel,
    nav: (String) -> Unit,
    content: @Composable (AccountViewModel) -> Unit
) {
    Column(modifier = BottomTopHeight) {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(Modifier) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            content(accountViewModel)
                        }
                    }
                }
            },
            navigationIcon = {
                val coroutineScope = rememberCoroutineScope()
                LoggedInUserPictureDrawer(accountViewModel) {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            },
            actions = {
                SearchButton() {
                    nav(Route.Search.route)
                }
            }
        )
        Divider(thickness = DividerThickness)
    }
}

@Composable
private fun SearchButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick
    ) {
        SearchIcon(modifier = Size22Modifier, Color.Unspecified)
    }
}

@Composable
private fun LoggedInUserPictureDrawer(
    accountViewModel: AccountViewModel,
    onClick: () -> Unit
) {
    val profilePicture by accountViewModel.account.userProfile().live().profilePictureChanges.observeAsState()
    val pubkeyHex = remember { accountViewModel.userProfile().pubkeyHex }

    val automaticallyShowProfilePicture = remember {
        when (accountViewModel.account.settings.automaticallyShowProfilePictures) {
            ConnectivityType.WIFI_ONLY -> !ConnectivityStatus.isOnMobileData.value
            ConnectivityType.NEVER -> false
            ConnectivityType.ALWAYS -> true
        }
    }

    IconButton(
        onClick = onClick
    ) {
        RobohashAsyncImageProxy(
            robot = pubkeyHex,
            model = profilePicture,
            contentDescription = stringResource(id = R.string.profile_image),
            modifier = HeaderPictureModifier,
            contentScale = ContentScale.Crop,
            loadProfilePicture = automaticallyShowProfilePicture
        )
    }
}

@Composable
fun FollowListWithRoutes(
    followListsModel: FollowListViewModel,
    listName: String,
    onChange: (CodeName) -> Unit
) {
    val allLists by followListsModel.kind3GlobalPeopleRoutes.collectAsState()

    SimpleTextSpinner(
        placeholderCode = listName,
        options = allLists,
        onSelect = {
            onChange(allLists.getOrNull(it) ?: followListsModel.kind3Follow)
        }
    )
}

@Composable
fun FollowListWithoutRoutes(
    followListsModel: FollowListViewModel,
    listName: String,
    onChange: (CodeName) -> Unit
) {
    val allLists by followListsModel.kind3GlobalPeople.collectAsState()

    SimpleTextSpinner(
        placeholderCode = listName,
        options = allLists,
        onSelect = {
            onChange(allLists.getOrNull(it) ?: followListsModel.kind3Follow)
        }
    )
}

enum class CodeNameType {
    HARDCODED, PEOPLE_LIST, ROUTE
}

abstract class Name {
    abstract fun name(): String
    open fun name(context: Context) = name()
}

class GeoHashName(val geoHashTag: String) : Name() {
    override fun name() = "/g/$geoHashTag"
}
class HashtagName(val hashTag: String) : Name() {
    override fun name() = "#$hashTag"
}
class ResourceName(val resourceId: Int) : Name() {
    override fun name() = " $resourceId " // Space to make sure it goes first
    override fun name(context: Context) = context.getString(resourceId)
}

class PeopleListName(val note: AddressableNote) : Name() {
    override fun name() = (note.event as? PeopleListEvent)?.nameOrTitle() ?: note.dTag() ?: ""
}
class CommunityName(val note: AddressableNote) : Name() {
    override fun name() = "/n/${(note.dTag() ?: "")}"
}

@Immutable
data class CodeName(val code: String, val name: Name, val type: CodeNameType)

@Stable
class FollowListViewModel(val account: Account) : ViewModel() {
    val kind3Follow = CodeName(KIND3_FOLLOWS, ResourceName(R.string.follow_list_kind3follows), CodeNameType.HARDCODED)
    val globalFollow = CodeName(GLOBAL_FOLLOWS, ResourceName(R.string.follow_list_global), CodeNameType.HARDCODED)

    private var _kind3GlobalPeopleRoutes = MutableStateFlow<ImmutableList<CodeName>>(emptyList<CodeName>().toPersistentList())
    val kind3GlobalPeopleRoutes = _kind3GlobalPeopleRoutes.asStateFlow()

    private var _kind3GlobalPeople = MutableStateFlow<ImmutableList<CodeName>>(emptyList<CodeName>().toPersistentList())
    val kind3GlobalPeople = _kind3GlobalPeople.asStateFlow()

    fun refresh() {
        viewModelScope.launch(Dispatchers.Default) {
            refreshFollows()
        }
    }

    private suspend fun refreshFollows() {
        checkNotInMainThread()

        val newFollowLists = LocalCache.addressables.mapNotNull {
            val event = (it.value.event as? PeopleListEvent)
            // Has to have an list
            if (event != null &&
                event.pubKey == account.userProfile().pubkeyHex &&
                (event.tags.size > 1 || event.content.length > 50)
            ) {
                CodeName(event.address().toTag(), PeopleListName(it.value), CodeNameType.PEOPLE_LIST)
            } else {
                null
            }
        }.sortedBy { it.name.name() }

        val communities = account.userProfile().cachedFollowingCommunitiesSet().mapNotNull {
            LocalCache.checkGetOrCreateAddressableNote(it)?.let { communityNote ->
                CodeName("Community/${communityNote.idHex}", CommunityName(communityNote), CodeNameType.ROUTE)
            }
        }

        val hashtags = account.userProfile().cachedFollowingTagSet().map {
            CodeName("Hashtag/$it", HashtagName(it), CodeNameType.ROUTE)
        }

        val geotags = account.userProfile().cachedFollowingGeohashSet().map {
            CodeName("Geohash/$it", GeoHashName(it), CodeNameType.ROUTE)
        }

        val routeList = (communities + hashtags + geotags).sortedBy { it.name.name() }

        val kind3GlobalPeopleRouteList = listOf(listOf(kind3Follow, globalFollow), newFollowLists, routeList).flatten().toImmutableList()

        if (!equalImmutableLists(_kind3GlobalPeopleRoutes.value, kind3GlobalPeopleRouteList)) {
            _kind3GlobalPeopleRoutes.emit(kind3GlobalPeopleRouteList)
        }

        val kind3GlobalPeopleList = listOf(listOf(kind3Follow, globalFollow), newFollowLists).flatten().toImmutableList()

        if (!equalImmutableLists(_kind3GlobalPeople.value, kind3GlobalPeopleList)) {
            _kind3GlobalPeople.emit(kind3GlobalPeopleList)
        }
    }

    var collectorJob: Job? = null

    init {
        Log.d("Init", "App Top Bar")
        refresh()
        collectorJob = viewModelScope.launch(Dispatchers.IO) {
            LocalCache.live.newEventBundles.collect { newNotes ->
                checkNotInMainThread()
                if (newNotes.any {
                    it.event?.pubKey() == account.userProfile().pubkeyHex && (it.event is PeopleListEvent || it.event is ContactListEvent)
                }
                ) {
                    refresh()
                }
            }
        }
    }

    override fun onCleared() {
        collectorJob?.cancel()
        super.onCleared()
    }

    class Factory(val account: Account) : ViewModelProvider.Factory {
        override fun <FollowListViewModel : ViewModel> create(modelClass: Class<FollowListViewModel>): FollowListViewModel {
            return FollowListViewModel(account) as FollowListViewModel
        }
    }
}

@Composable
fun SimpleTextSpinner(
    placeholderCode: String,
    options: ImmutableList<CodeName>,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    var optionsShowing by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val selectAnOption = stringResource(
        id = R.string.select_an_option
    )

    var currentText by remember {
        mutableStateOf(
            options.firstOrNull { it.code == placeholderCode }?.name?.name(context) ?: selectAnOption
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.size(20.dp))
            Text(currentText)
            Icon(
                imageVector = Icons.Default.ExpandMore,
                null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.placeholderText
            )
        }
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    optionsShowing = true
                }
        )
    }

    if (optionsShowing) {
        options.isNotEmpty().also {
            SpinnerSelectionDialog(
                options = options,
                onDismiss = { optionsShowing = false },
                onSelect = {
                    currentText = options[it].name.name(context)
                    optionsShowing = false
                    onSelect(it)
                }
            ) {
                RenderOption(it.name)
            }
        }
    }
}

@Composable
fun RenderOption(option: Name) {
    when (option) {
        is GeoHashName -> {
            LoadCityName(option.geoHashTag) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "/g/$it", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
        is HashtagName -> {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = option.name(), color = MaterialTheme.colorScheme.onSurface)
            }
        }
        is ResourceName -> {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = option.resourceId), color = MaterialTheme.colorScheme.onSurface)
            }
        }
        is PeopleListName -> {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = option.name(), color = MaterialTheme.colorScheme.onSurface)
            }
        }
        is CommunityName -> {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                val name by option.note.live().metadata.map {
                    "/n/" + (it.note as? AddressableNote)?.dTag()
                }.observeAsState()

                Text(text = name ?: "", color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithBackButton(caption: String, popBack: () -> Unit) {
    Column(modifier = BottomTopHeight) {
        TopAppBar(
            title = { Text(caption) },
            navigationIcon = {
                IconButton(
                    onClick = popBack,
                    modifier = Modifier
                ) {
                    ArrowBackIcon()
                }
            },
            actions = {}
        )
        Divider(thickness = 0.25.dp)
    }
}

@Composable
fun FlexibleTopBarWithBackButton(
    title: @Composable RowScope.() -> Unit,
    extendableRow: (@Composable () -> Unit)? = null,
    popBack: () -> Unit
) {
    Column() {
        MyExtensibleTopAppBar(
            title = title,
            extendableRow = extendableRow,
            navigationIcon = {
                IconButton(
                    onClick = popBack,
                    modifier = Modifier
                ) {
                    ArrowBackIcon()
                }
            },
            actions = {}
        )
        Spacer(modifier = HalfVertSpacer)
        Divider(thickness = 0.25.dp)
    }
}

@Composable
fun AmethystClickableIcon() {
    val context = LocalContext.current

    IconButton(
        onClick = {
            debugState(context)
        }
    ) {
        AmethystIcon(Size40dp)
    }
}

fun debugState(context: Context) {
    Client.allSubscriptions().map {
        "$it ${
        Client.getSubscriptionFilters(it)
            .joinToString { it.filter.toJson() }
        }"
    }.forEach {
        Log.d("STATE DUMP", it)
    }

    NostrAccountDataSource.printCounter()
    NostrChannelDataSource.printCounter()
    NostrChatroomDataSource.printCounter()
    NostrChatroomListDataSource.printCounter()
    NostrCommunityDataSource.printCounter()
    NostrDiscoveryDataSource.printCounter()
    NostrHashtagDataSource.printCounter()
    NostrGeohashDataSource.printCounter()
    NostrHomeDataSource.printCounter()
    NostrSearchEventOrUserDataSource.printCounter()
    NostrSingleChannelDataSource.printCounter()
    NostrSingleEventDataSource.printCounter()
    NostrSingleUserDataSource.printCounter()
    NostrThreadDataSource.printCounter()
    NostrUserProfileDataSource.printCounter()
    NostrVideoDataSource.printCounter()

    Log.d("STATE DUMP", "Connected Relays: " + RelayPool.connectedRelays())

    val imageLoader = Coil.imageLoader(context)
    Log.d("STATE DUMP", "Image Disk Cache ${(imageLoader.diskCache?.size ?: 0) / (1024 * 1024)}/${(imageLoader.diskCache?.maxSize ?: 0) / (1024 * 1024)} MB")
    Log.d("STATE DUMP", "Image Memory Cache ${(imageLoader.memoryCache?.size ?: 0) / (1024 * 1024)}/${(imageLoader.memoryCache?.maxSize ?: 0) / (1024 * 1024)} MB")

    Log.d("STATE DUMP", "Notes: " + LocalCache.notes.filter { it.value.liveSet != null }.size + " / " + LocalCache.notes.filter { it.value.event != null }.size + " / " + LocalCache.notes.size)
    Log.d("STATE DUMP", "Addressables: " + LocalCache.addressables.filter { it.value.liveSet != null }.size + " / " + LocalCache.addressables.filter { it.value.event != null }.size + " / " + LocalCache.addressables.size)
    Log.d("STATE DUMP", "Users: " + LocalCache.users.filter { it.value.liveSet != null }.size + " / " + LocalCache.users.filter { it.value.info?.latestMetadata != null }.size + " / " + LocalCache.users.size)

    Log.d("STATE DUMP", "Memory used by Events: " + LocalCache.notes.values.sumOf { it.event?.countMemory() ?: 0 } / (1024 * 1024) + " MB")

    LocalCache.notes.values.groupBy { it.event?.kind() }.forEach {
        Log.d("STATE DUMP", "Kind ${it.key}: \t${it.value.size} elements ")
    }
    LocalCache.addressables.values.groupBy { it.event?.kind() }.forEach {
        Log.d("STATE DUMP", "Kind ${it.key}: \t${it.value.size} elements ")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyExtensibleTopAppBar(
    title: @Composable RowScope.() -> Unit,
    extendableRow: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {}
) {
    val expanded = remember { mutableStateOf(false) }

    Column(
        Modifier.clickable {
            expanded.value = !expanded.value
        }
    ) {
        Row(modifier = BottomTopHeight) {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        title()
                    }
                },
                modifier = modifier,
                navigationIcon = {
                    if (navigationIcon == null) {
                        Spacer(TitleInsetWithoutIcon)
                    } else {
                        Row(TitleIconModifier, verticalAlignment = Alignment.CenterVertically) {
                            navigationIcon()
                        }
                    }
                },
                actions = actions
            )
        }

        if (expanded.value && extendableRow != null) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    extendableRow()
                }
            }
        }
    }
}

private val AppBarHeight = 50.dp

// TODO: this should probably be part of the touch target of the start and end icons, clarify this
private val AppBarHorizontalPadding = 4.dp

// Start inset for the title when there is no navigation icon provided
private val TitleInsetWithoutIcon = Modifier.width(16.dp - AppBarHorizontalPadding)

// Start inset for the title when there is a navigation icon provided
private val TitleIconModifier = Modifier.width(48.dp - AppBarHorizontalPadding)
