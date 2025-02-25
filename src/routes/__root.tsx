import {createRootRoute, Outlet} from '@tanstack/react-router'
import {TanStackRouterDevtools} from '@tanstack/router-devtools'
import ResponsiveAppBar from "../components/ResponsiveAppBar/ResponsiveAppBar.tsx";
import {ReactQueryDevtools} from "@tanstack/react-query-devtools";

export const Route = createRootRoute({
    component: () => (
        <>
            <header>
                <ResponsiveAppBar/>
            </header>
            <Outlet/>
            <ReactQueryDevtools buttonPosition="top-right"/>
            <TanStackRouterDevtools/>
        </>
    ),
})
