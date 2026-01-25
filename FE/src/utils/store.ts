import { defineStore } from 'pinia'

export const useProjectStore = defineStore('idProject', {
  state: () => ({
    idProject: null as string | null,
    idPhase: null as string | null,
  }),
  actions: {
    setIdProject(project: string, phase: string) {
      this.idProject = project
      this.idPhase = phase
    },
    clearPhase() {
      this.idProject = null
      this.idPhase = null
    },
  },
})
