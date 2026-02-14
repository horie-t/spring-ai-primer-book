import useSWR from 'swr'; // SWRなどのデータ取得ライブラリを使用

const fetcher = (url: string) => fetch(url, {credentials: 'include',}).then(res => {
    if (!res.ok) {
        throw new Error('Not authenticated');
    }
    return res.json();
});

export function useAuth() {
    const { data: user, error, isLoading } = useSWR('http://localhost:8080/api/user/me', fetcher, {
        revalidateOnFocus: false,
        revalidateOnReconnect: false,
        shouldRetryOnError: false,
    });
    const isAuthenticated = !!user && !error;

    return {
        user: isAuthenticated ? user : null,
        isLoading,
        isAuthenticated,
    };
}
