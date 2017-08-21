/**
*
*
*
*
*
*
*/

#define REPF( i , a , b ) for ( int i = a ; i <= b ; ++ i )
#define REPV( i , a , b ) for ( int i = a ; i >= b ; -- i )
#define LOGF( i , n ) for ( int i = 1 ; ( 1 << i ) < n ; ++ i )
#define EDGE( i , x ) for ( int i = adj[x] ; ~i ; i = edge[i].n )
#define clear( a , x ) memset ( a , x , sizeof a )

const int MAXN = 40005 ;
const int MAXE = 80005 ;
const int LOGN = 20 ;

struct Edge {
    int v , w , n ;
    Edge ( int var = 0 , int cost = 0 , int next = 0 ) :
        v ( var ) , w ( cost ) , n ( next ) {}
} ;

struct LCA {
    Edge edge[MAXE] ;
    int adj[MAXN] , cntE ;
    int deep[MAXN] ;
    int dist[MAXN] ;
    int anc[MAXN][LOGN] ;
    int fa[MAXN] ;
    int n ;

    void init () {
        clear ( adj , -1 ) ;
        cntE = 0 ;
    }

    void addedge ( int u , int v , int w ) {
        edge[cntE] = Edge ( v , w , adj[u] ) ;
        adj[u] = cntE ++ ;
        edge[cntE] = Edge ( u , w , adj[v] ) ;
        adj[v] = cntE ++ ;
    }

    void dfs ( int u , int p ) {
        EDGE ( i , u ) {
            int v = edge[i].v ;
            if ( v != p ) {
                fa[v] = u ;
                deep[v] = deep[u] + 1 ;
                dist[v] = dist[u] + edge[i].w ;
                dfs( v , u ) ;
            }
        }
    }

    void preProcess () {
        REPF ( i , 1 , n ) {
            anc[i][0] = fa[i] ;
            LOGF ( j , n )
                anc[i][j] = -1 ;
        }
        LOGF ( j , n )
            REPF ( i , 1 , n )
                if ( ~anc[i][j - 1] ) {
                    int u = anc[i][j - 1] ;
                    anc[i][j] = anc[u][j - 1] ;
                }
    }

    int query ( int p , int q ) {
        int log = 0 ;
        if ( deep[p] < deep[q] )
            swap ( p , q ) ;
        LOGF ( i , deep[p] + 1 )
            ++ log ;
        REPV ( i , log , 0 )
            if ( deep[p] - ( 1 << i ) >= deep[q] ) {
                p = anc[p][i] ;
            }
        if ( p == q )
            return p ;
        REPV ( i , log , 0 )
            if ( ~anc[p][i] && anc[p][i] != anc[q][i] ) {
                p = anc[p][i] ;
                q = anc[q][i] ;
            }
        return fa[p] ;
    }
} ;

LCA L ;

void work () {
    int u , v , w , m ;
    scanf ( "%d%d" , &L.n , &m ) ;
    L.init () ;
    REPF ( i , 1 , L.n - 1 ) {
        scanf ( "%d%d%d" , &u , &v , &w ) ;
        L.addedge ( u , v , w ) ;
    }
    L.dist[1] = 0 ;
    L.dfs ( 1 , 0 ) ;
    L.preProcess () ;
    while ( m -- ) {
        scanf ( "%d%d" , &u , &v ) ;
        printf ( "%d\n" , L.dist[u] + L.dist[v] - 2 * L.dist[L.query ( u , v )] ) ;
    }
}