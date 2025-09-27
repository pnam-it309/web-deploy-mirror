<script setup lang="ts">
import { ROUTES_CONSTANTS } from '@/constants/path'
import { DoubleLeftOutlined } from '@ant-design/icons-vue'
import { ref } from 'vue'
import { useRoute } from 'vue-router'

const isCollapsed = ref(false)
const isMobileMenuOpen = ref(false)

const route = useRoute()

const menuItems = ref([
  {
    icon: `<svg version="1.1" id="Icons" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 32 32" xml:space="preserve" fill="#000000"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <style type="text/css"> .st0{fill:none;stroke:#000000;stroke-width:2;stroke-linecap:round;stroke-linejoin:round;stroke-miterlimit:10;} </style> <path class="st0" d="M28,9H14c-1.7,0-3,1.3-3,3v0c0,1.7,1.3,3,3,3h14v15"></path> <path class="st0" d="M28,30H14c-1.7,0-3-1.3-3-3v0V12"></path> <line class="st0" x1="28" y1="12" x2="14" y2="12"></line> <path class="st0" d="M21,3H7C5.3,3,4,4.3,4,6v0c0,1.7,1.3,3,3,3h8"></path> <path class="st0" d="M11,24H7c-1.7,0-3-1.3-3-3v0V6"></path> <line class="st0" x1="21" y1="6" x2="7" y2="6"></line> </g></svg>`,
    label: 'Quản lý bộ môn',
    routeName: ROUTES_CONSTANTS.ADMIN.children.DEPARTMENT.name,
  },
  {
    icon: `<svg viewBox="-2.4 -2.4 28.80 28.80" fill="none" xmlns="http://www.w3.org/2000/svg" transform="rotate(0)"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round" stroke="#CCCCCC" stroke-width="0.048"></g><g id="SVGRepo_iconCarrier"> <path d="M3 21H21" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path> <path d="M19 21V15V7C19 5.11438 19 4.17157 18.4142 3.58579C17.8284 3 16.8856 3 15 3H12H9C7.11438 3 6.17157 3 5.58579 3.58579C5 4.17157 5 5.11438 5 7V15V21" stroke="#000000" stroke-width="2" stroke-linejoin="round"></path> <path d="M9 8L10 8" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path> <path d="M9 12L10 12" stroke="#ffffff " stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path> <path d="M9 16L10 16" stroke="#ffffff " stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path> <path d="M14 8L15 8" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path> <path d="M14 12L15 12" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path> <path d="M14 16L15 16" stroke="#000000" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"></path> </g></svg>`,
    label: 'Quản lý cơ sở',
    routeName: ROUTES_CONSTANTS.ADMIN.children.FACILITY.name,
  },
  {
    icon: `<svg fill="#000000" version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 512.002 512.002" xml:space="preserve"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <g> <g> <g> <path d="M317.143,444.752c-0.775-3.101-3.562-5.284-6.759-5.284H201.616c-3.197,0-5.985,2.182-6.759,5.283 c-7.876,31.546-12.131,48.723-14.469,58.702c-1.024,4.372,2.294,8.547,6.784,8.547h137.654c4.489,0,7.807-4.174,6.784-8.544 C329.291,493.545,325.046,476.404,317.143,444.752z"></path> <path d="M189.856,352.876c-17.271-6.304-33.919-15.116-49.648-26.949c-2.971-2.236-7.162-1.776-9.547,1.077L37.381,438.586 c-24.366,29.146-3.791,73.415,35.301,73.415h78.805c3.29,0,6.124-2.306,6.812-5.524c3.059-14.314,13.917-57.282,35.927-145.371 C195.09,357.65,193.203,354.097,189.856,352.876z"></path> <path d="M297.659,366.797c-0.887-3.55-4.37-5.81-7.973-5.168c-21.566,3.844-44.191,4.132-67.372,0 c-3.603-0.642-7.085,1.619-7.973,5.168c-3.908,15.641-7.404,29.622-10.523,42.097l-0.256,1.023 c-1.099,4.397,2.226,8.655,6.756,8.655h91.36c4.529,0,7.858-4.247,6.761-8.641c-0.116-0.467-0.234-0.933-0.351-1.404 C304.995,396.148,301.529,382.288,297.659,366.797z"></path> <path d="M474.622,438.586l-93.281-111.583c-2.385-2.852-6.575-3.311-9.547-1.077c-15.087,11.349-31.622,20.37-49.649,26.949 c-3.348,1.222-5.234,4.774-4.37,8.232c21.979,87.96,32.931,131.32,35.928,145.396c0.684,3.21,3.527,5.497,6.808,5.497h78.809 C478.459,512,498.966,467.703,474.622,438.586z"></path> <path d="M101.086,110.267c2.691-0.501-5.292-0.35,115.908-0.35c11.54,0,20.896,9.356,20.896,20.896v20.626 c0,1.994,1.616,3.608,3.608,3.608h29.004c1.992,0,3.608-1.616,3.608-3.608v-20.626c0-11.54,9.356-20.896,20.896-20.896 c120.981,0,113.202-0.156,115.91,0.35c2.797,0.521,5.019-2.296,3.948-4.933C357.801-35.119,154.222-35.103,97.14,105.332 C96.067,107.969,98.289,110.786,101.086,110.267z"></path> <path d="M410.567,247.156c1.191-2.432-0.66-5.184-3.368-5.174c-0.025,0-0.05,0-0.077,0H295.007 c-11.54,0-20.896-9.356-20.896-20.896v-20.637c0-1.993-1.616-3.608-3.608-3.608h-29.004c-1.992,0-3.608,1.615-3.608,3.608v20.637 c0,11.54-9.356,20.896-20.896,20.896H104.879c-0.025,0-0.05,0-0.077,0c-2.708-0.01-4.56,2.742-3.368,5.174 C164.328,375.641,347.639,375.717,410.567,247.156z"></path> <path d="M400.157,221.085c3.846,0,6.965-3.119,6.965-6.965v-76.341c0-3.846-3.119-6.965-6.965-6.965h-98.184 c-3.846,0-6.965,3.119-6.965,6.965v76.341c0,3.846,3.119,6.965,6.965,6.965H400.157z"></path> <path d="M210.029,221.085c3.846,0,6.965-3.119,6.965-6.965v-76.341c0-3.846-3.119-6.965-6.965-6.965h-98.184 c-3.846,0-6.965,3.119-6.965,6.965v76.341c0,3.846,3.119,6.965,6.965,6.965H210.029z"></path> </g> </g> </g> </g></svg>`,
    label: 'Quản lý nhân viên',
    routeName: ROUTES_CONSTANTS.ADMIN.children.STAFF.name,
  },
  {
    icon: `<svg viewBox="0 0 600 600" version="1.1" id="svg9724" sodipodi:docname="people.svg" inkscape:version="1.2.2 (1:1.2.2+202212051550+b0a8486541)" xmlns:inkscape="http://www.inkscape.org/namespaces/inkscape" xmlns:sodipodi="http://sodipodi.sourceforge.net/DTD/sodipodi-0.dtd" xmlns="http://www.w3.org/2000/svg" xmlns:svg="http://www.w3.org/2000/svg" fill="#000000"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <defs id="defs9728"></defs> <sodipodi:namedview id="namedview9726" pagecolor="#ffffff" bordercolor="#666666" borderopacity="1.0" inkscape:showpageshadow="2" inkscape:pageopacity="0.0" inkscape:pagecheckerboard="0" inkscape:deskcolor="#d1d1d1" showgrid="true" inkscape:zoom="0.84118632" inkscape:cx="319.19207" inkscape:cy="427.37262" inkscape:window-width="1920" inkscape:window-height="1009" inkscape:window-x="0" inkscape:window-y="1080" inkscape:window-maximized="1" inkscape:current-layer="g10449" showguides="true"> <inkscape:grid type="xygrid" id="grid9972" originx="0" originy="0"></inkscape:grid> <sodipodi:guide position="-260,300" orientation="0,-1" id="guide383" inkscape:locked="false"></sodipodi:guide> <sodipodi:guide position="300,520" orientation="1,0" id="guide385" inkscape:locked="false"></sodipodi:guide> <sodipodi:guide position="240,520" orientation="0,-1" id="guide939" inkscape:locked="false"></sodipodi:guide> <sodipodi:guide position="220,80" orientation="0,-1" id="guide941" inkscape:locked="false"></sodipodi:guide> </sodipodi:namedview> <g id="g10449" transform="matrix(0.95173205,0,0,0.95115787,13.901174,12.168794)" style="stroke-width:1.05103"> <g id="path10026" inkscape:transform-center-x="-0.59233046" inkscape:transform-center-y="-20.347403" transform="matrix(1.3807551,0,0,1.2700888,273.60014,263.99768)"></g> <g id="g11314" transform="matrix(1.5092301,0,0,1.3955555,36.774048,-9.4503933)" style="stroke-width:50.6951"></g> <path style="color:#000000;fill:#000000;stroke-width:1.05103;stroke-linecap:round;stroke-linejoin:round;-inkscape-stroke:none;paint-order:stroke fill markers" d="m 248.07279,-12.793664 c -72.13241,0 -131.33949,59.250935 -131.33949,131.392074 0,38.92115 17.25502,74.07152 44.45432,98.20884 C 58.500207,254.84854 -14.606185,358.21398 -14.606185,477.846 a 35.037921,35.037921 0 0 0 35.034809,35.03543 H 188.95771 c 6.88866,-25.46243 17.91968,-49.15043 32.45932,-70.0688 H 58.235927 C 73.730605,344.39181 153.38526,271.2598 248.07279,271.2598 c 13.12286,0 25.94065,1.45153 38.35524,4.13353 4.26325,-42.80875 34.59589,-78.30933 74.73011,-90.32371 11.57931,-19.5408 18.25414,-42.27592 18.25414,-66.47121 0,-72.141139 -59.20709,-131.392074 -131.33949,-131.392074 z m 0,70.068794 c 34.24293,0 61.26987,27.028459 61.26987,61.32328 0,34.29482 -27.02694,61.3274 -61.26987,61.3274 -34.24293,0 -61.27192,-27.03258 -61.27192,-61.3274 0,-34.294821 27.02899,-61.32328 61.27192,-61.32328 z" id="path295"></path> <path id="path295-3" style="color:#000000;fill:#000000;stroke-width:1.05103;stroke-linecap:round;stroke-linejoin:round;-inkscape-stroke:none;paint-order:stroke fill markers" d="m 405.68024,197.47637 c -57.70598,0 -105.07159,47.40151 -105.07159,105.11449 0,31.13694 13.80343,59.25664 35.56289,78.56652 -82.15001,30.43306 -140.63449,113.12556 -140.63449,208.83127 a 28.030337,28.030337 0 0 0 28.0273,28.0278 h 182.11589 182.11452 a 28.030337,28.030337 0 0 0 28.0286,-28.0278 c 0,-95.70539 -58.4835,-178.39795 -140.63307,-208.83127 21.75947,-19.30988 35.56153,-47.42958 35.56153,-78.56652 0,-57.71298 -47.3656,-105.11449 -105.07158,-105.11449 z m 0,56.05559 c 27.39437,0 49.01562,21.62301 49.01562,49.0589 0,27.43588 -21.62125,49.06164 -49.01562,49.06164 -27.39437,0 -49.017,-21.62576 -49.017,-49.06164 0,-27.43589 21.62263,-49.0589 49.017,-49.0589 z m 0,171.18664 c 75.7501,0 139.47372,58.50552 151.86952,137.24226 H 405.68024 253.81075 C 266.2065,483.22412 329.93014,424.7186 405.68024,424.7186 Z"></path> </g> </g></svg>`,
    label: 'Quản lý sinh viên',
    routeName: ROUTES_CONSTANTS.ADMIN.children.STUDENT.name,
  },
  {
    icon: `<svg fill="#000000" viewBox="0 0 52 52" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path d="M38.3,27.2A11.4,11.4,0,1,0,49.7,38.6,11.46,11.46,0,0,0,38.3,27.2Zm2,12.4a2.39,2.39,0,0,1-.9-.2l-4.3,4.3a1.39,1.39,0,0,1-.9.4,1,1,0,0,1-.9-.4,1.39,1.39,0,0,1,0-1.9l4.3-4.3a2.92,2.92,0,0,1-.2-.9,3.47,3.47,0,0,1,3.4-3.8,2.39,2.39,0,0,1,.9.2c.2,0,.2.2.1.3l-2,1.9a.28.28,0,0,0,0,.5L41.1,37a.38.38,0,0,0,.6,0l1.9-1.9c.1-.1.4-.1.4.1a3.71,3.71,0,0,1,.2.9A3.57,3.57,0,0,1,40.3,39.6Z"></path> <circle cx="21.7" cy="14.9" r="12.9"></circle> <path d="M25.2,49.8c2.2,0,1-1.5,1-1.5h0a15.44,15.44,0,0,1-3.4-9.7,15,15,0,0,1,1.4-6.4.77.77,0,0,1,.2-.3c.7-1.4-.7-1.5-.7-1.5h0a12.1,12.1,0,0,0-1.9-.1A19.69,19.69,0,0,0,2.4,47.1c0,1,.3,2.8,3.4,2.8H24.9C25.1,49.8,25.1,49.8,25.2,49.8Z"></path> </g></svg>`,

    label: 'Quản lý chức vụ',
    routeName: ROUTES_CONSTANTS.ADMIN.children.ROLE.name,
  },

  {
    icon: `<svg fill="#000000" viewBox="0 0 52 52" data-name="Layer 1" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <path d="M38.3,27.2A11.4,11.4,0,1,0,49.7,38.6,11.46,11.46,0,0,0,38.3,27.2Zm2,12.4a2.39,2.39,0,0,1-.9-.2l-4.3,4.3a1.39,1.39,0,0,1-.9.4,1,1,0,0,1-.9-.4,1.39,1.39,0,0,1,0-1.9l4.3-4.3a2.92,2.92,0,0,1-.2-.9,3.47,3.47,0,0,1,3.4-3.8,2.39,2.39,0,0,1,.9.2c.2,0,.2.2.1.3l-2,1.9a.28.28,0,0,0,0,.5L41.1,37a.38.38,0,0,0,.6,0l1.9-1.9c.1-.1.4-.1.4.1a3.71,3.71,0,0,1,.2.9A3.57,3.57,0,0,1,40.3,39.6Z"></path> <circle cx="21.7" cy="14.9" r="12.9"></circle> <path d="M25.2,49.8c2.2,0,1-1.5,1-1.5h0a15.44,15.44,0,0,1-3.4-9.7,15,15,0,0,1,1.4-6.4.77.77,0,0,1,.2-.3c.7-1.4-.7-1.5-.7-1.5h0a12.1,12.1,0,0,0-1.9-.1A19.69,19.69,0,0,0,2.4,47.1c0,1,.3,2.8,3.4,2.8H24.9C25.1,49.8,25.1,49.8,25.2,49.8Z"></path> </g></svg>`,

    label: 'Quản lý lớp môn',
    routeName: ROUTES_CONSTANTS.ADMIN.children.CLASS_SUBJECT.name,
  },
  {
    icon: `<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><rect x="3" y="4" width="18" height="18" rx="2" stroke="#000" stroke-width="2"/><path d="M16 2v4M8 2v4M3 10h18" stroke="#000" stroke-width="2" stroke-linecap="round"/></svg>`,
    label: 'Quản lý học kỳ',
    routeName: ROUTES_CONSTANTS.ADMIN.children.SEMESTER.name,
  },

  {
    icon: `<svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><rect x="3" y="4" width="18" height="18" rx="2" stroke="#000" stroke-width="2"/><path d="M16 2v4M8 2v4M3 10h18" stroke="#000" stroke-width="2" stroke-linecap="round"/></svg>`,
    label: 'Quản lý môn học',
    routeName: ROUTES_CONSTANTS.ADMIN.children.SUBJECT.name,
  },
])
</script>

<template>
  <!-- Nút mở menu Mobile -->
  <button
    v-if="!isMobileMenuOpen"
    @click="isMobileMenuOpen = true"
    class="fixed top-4 left-4 z-50 flex items-center justify-center w-10 h-10 rounded-full bg-gray-800 hover:bg-gray-700 text-white shadow-lg transition transform hover:scale-105 active:scale-95 lg:hidden"
  >
    <svg class="w-6 h-6" fill="none" stroke="currentColor" stroke-width="2" viewBox="0 0 24 24">
      <path stroke-linecap="round" stroke-linejoin="round" d="M4 6h16M4 12h16M4 18h16" />
    </svg>
  </button>

  <!-- SIDEBAR -->
  <aside
    class="fixed inset-0 lg:inset-y-0 lg:left-0 z-40 flex h-screen flex-col bg-white transition-all duration-300 ease-in-out lg:relative lg:translate-x-0"
    :class="{
      'hidden lg:block': !isMobileMenuOpen,
      'translate-x-0 w-[80%]': isMobileMenuOpen,
      'w-20': isCollapsed && !isMobileMenuOpen,
      'w-64': !isCollapsed && !isMobileMenuOpen,
    }"
  >
    <!-- Logo và nút thu gọn -->
    <div class="flex items-center justify-between px-4 py-4 bg-white">
      <img
        src="../../../assets/images/logo-udpm-dark.png"
        alt="Logo"
        class="transition-all duration-300"
        :class="{ hidden: isCollapsed }"
        style="width: 180px; height: auto"
      />

      <button
        v-if="!isMobileMenuOpen"
        @click="isCollapsed = !isCollapsed"
        class="hidden lg:flex items-center justify-center w-10 h-10 rounded-full hover:bg-gray-300"
      >
        <DoubleLeftOutlined
          :style="{ transform: isCollapsed ? 'rotate(180deg)' : 'rotate(0deg)' }"
        />
      </button>

      <button
        v-if="isMobileMenuOpen"
        @click="isMobileMenuOpen = false"
        class="lg:hidden flex items-center justify-center w-8 h-8"
      >
        <svg
          class="w-6 h-6 text-black-2"
          viewBox="0 0 24 24"
          stroke="currentColor"
          stroke-width="2"
        >
          <path stroke-linecap="round" stroke-linejoin="round" d="M15 19l-7-7 7-7" />
        </svg>
      </button>
    </div>

    <!-- Menu chính -->
    <div class="px-4 py-2">
      <nav>
        <ul>
          <li v-for="(menuItem, index) in menuItems" :key="index">
            <router-link
              :to="{ name: menuItem.routeName }"
              class="group flex items-center gap-4 p-3 rounded-lg transition-all duration-200 hover:bg-hoverSideBar"
              :class="{ 'bg-selectedSideBar': route.name === menuItem.routeName }"
            >
              <span
                v-html="menuItem.icon"
                class="w-6 h-6"
                :class="{
                  'text-iconSideBarSelected': route.name === menuItem.routeName,
                  'text-iconSideBarIsNoSelected group-hover:text-iconSideBarSelected':
                    route.name !== menuItem.routeName,
                }"
              ></span>

              <span
                v-if="!isCollapsed"
                class="text-sm font-bold"
                :class="{
                  'text-textSideBarSelected': route.name === menuItem.routeName,
                  'text-textSideBarIsNoSelected group-hover:text-textSideBarSelected':
                    route.name !== menuItem.routeName,
                }"
              >
                {{ menuItem.label }}
              </span>
            </router-link>
          </li>
        </ul>
      </nav>
    </div>
  </aside>
</template>
<style scoped>
/* Smooth transition for sidebar width */
/* aside {
    transition: transform 0.2s ease-out, width 0.2s ease-out;
    will-change: transform, width, height;
  } */

/* Loại bỏ hiệu ứng hover trên logo */
.logo:hover {
  background: none;
}
</style>
