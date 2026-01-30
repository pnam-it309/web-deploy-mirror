import { defineStore } from 'pinia'
import { ref } from 'vue'
import { AppService } from '@/services/admin/app.service'
import type {
  AppResponse,
  AppCreateRequest,
  AppUpdateRequest,
  AppDetailResponse,
  AppDetailUpdateRequest,
} from '@/types/admin.types'

export const useAppStore = defineStore('app', () => {
  const apps = ref<AppResponse[]>([])
  const currentApp = ref<AppResponse | null>(null)
  const currentAppDetail = ref<AppDetailResponse | null>(null)

  const isLoading = ref(false)
  const totalElements = ref(0)
  const totalPages = ref(0)
  const filterParams = ref({
    page: 0,
    size: 10,
    keyword: '',
    domainId: '',
    status: null,
  })

  const fetchApps = async () => {
    isLoading.value = true
    try {
      const res = await AppService.getAll(filterParams.value)
      apps.value = res.content
      totalElements.value = res.totalElements
      totalPages.value = res.totalPages
    } catch (error) {
      throw error
    } finally {
      isLoading.value = false
    }
  }

  const getAppById = async (id: string) => {
    isLoading.value = true
    try {
      currentApp.value = await AppService.getById(id)
      try {
        currentAppDetail.value = await AppService.getDetailExtension(id)
      } catch {
        currentAppDetail.value = null
      }
    } catch (error) {
      throw error
    } finally {
      isLoading.value = false
    }
  }

  const createApp = async (payload: AppCreateRequest) => {
    isLoading.value = true
    try {
      return await AppService.create(payload)
    } catch (error) {
      throw error
    } finally {
      isLoading.value = false
    }
  }

  const updateApp = async (id: string, payload: AppUpdateRequest) => {
    isLoading.value = true
    try {
      const updatedApp = await AppService.update(id, payload)
      currentApp.value = updatedApp
      return updatedApp
    } catch (error) {
      throw error
    } finally {
      isLoading.value = false
    }
  }

  const updateAppDetailInfo = async (appId: string, payload: AppDetailUpdateRequest) => {
    isLoading.value = true
    try {
      const updatedDetail = await AppService.updateDetailExtension(appId, payload)
      currentAppDetail.value = updatedDetail
      return updatedDetail
    } catch (error) {
      throw error
    } finally {
      isLoading.value = false
    }
  }

  const deleteApp = async (id: string) => {
    isLoading.value = true
    try {
      await AppService.deleteApp(id)
      await fetchApps()
    } catch (error) {
      throw error
    } finally {
      isLoading.value = false
    }
  }

  const setFilter = (newFilter: any) => {
    filterParams.value = { ...filterParams.value, ...newFilter }
    fetchApps()
  }

  const changeStatus = async (id: string, status: string) => {
    isLoading.value = true
    try {
      await AppService.changeStatus(id, status)
    } catch (error) {
      throw error
    } finally {
      isLoading.value = false
    }
  }

  const duplicateApp = async (id: string) => {
    isLoading.value = true
    try {
      await AppService.duplicate(id)
      await fetchApps()
    } catch (error) {
      throw error
    } finally {
      isLoading.value = false
    }
  }

  const exportApps = async () => {
    isLoading.value = true
    try {
      const data = await AppService.exportApps()
      // Create download link
      const url = window.URL.createObjectURL(new Blob([data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', `apps_export_${new Date().getTime()}.xlsx`)
      document.body.appendChild(link)
      link.click()
      link.remove()
    } catch (error) {
      throw error
    } finally {
      isLoading.value = false
    }
  }

  const importApps = async (file: File) => {
    isLoading.value = true
    try {
      await AppService.importApps(file)
      await fetchApps()
    } catch (error) {
      throw error
    } finally {
      isLoading.value = false
    }
  }

  const downloadTemplate = async () => {
    isLoading.value = true
    try {
      const data = await AppService.downloadTemplate()
      const url = window.URL.createObjectURL(new Blob([data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', `apps_import_template.xlsx`)
      document.body.appendChild(link)
      link.click()
      link.remove()
    } catch (error) {
      throw error
    } finally {
      isLoading.value = false
    }
  }

  return {
    apps,
    currentApp,
    currentAppDetail,
    isLoading,
    totalElements,
    totalPages,
    filterParams,
    fetchApps,
    getAppById,
    createApp,
    updateApp,
    updateAppDetailInfo,
    deleteApp,
    setFilter,
    changeStatus,
    duplicateApp,
    exportApps,
    importApps,
    downloadTemplate,
  }
})
